package scodec

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

case class CodecMetadata[T](codec: Codec[T], name: String, tpeString: String)
object CodecMetadata {
  implicit def meta[T](codec: Codec[T]): CodecMetadata[T] = macro metadataMacro[T]

  def metadataMacro[T: ctx.WeakTypeTag](ctx: blackbox.Context)(codec: ctx.Expr[Codec[T]]): ctx.Expr[CodecMetadata[T]] = {
    import ctx.universe._

    val tree = codec.tree
    val name: String = tree match {
      case q"$qual.${ TermName(name) }" ⇒ name
    }

    val elementTpe = weakTypeOf[T]
    val tpe: String = elementTpe.typeSymbol.name.toString

    reify(CodecMetadata(codec.splice, ctx.literal(name).splice, ctx.literal(tpe).splice))
  }
}

case class ExpressionAndSyntax[T](value: T, syntax: String)
object ExpressionAndSyntax {
  implicit def expressionAndSyntax[T](value: T): ExpressionAndSyntax[T] = macro exprMacro[T]
  implicit def expressionAndSyntaxFromString[T](code: String): ExpressionAndSyntax[T] = macro exprFromStringMacro[T]

  def exprMacro[T: ctx.WeakTypeTag](ctx: blackbox.Context)(value: ctx.Expr[T]): ctx.Expr[ExpressionAndSyntax[T]] = {
    import ctx.universe._

    val pos = value.tree.pos
    if (!pos.isRange) println(s"Does not have a rangePos $pos $value")

    val str = new String(pos.source.content.drop(pos.start).take(pos.end - pos.start))

    reify(ExpressionAndSyntax(value.splice, ctx.literal(str).splice))
  }
  def exprFromStringMacro[T: ctx.WeakTypeTag](ctx: blackbox.Context)(code: ctx.Expr[String]): ctx.Expr[ExpressionAndSyntax[T]] = {
    import ctx.universe._
    val codeString = code.tree match {
      case Literal(Constant(str: String)) ⇒ str
    }
    val value = ctx.Expr(ctx.parse(codeString))

    def retypeCheck[T](e: ctx.Expr[T]): ctx.Expr[T] = ctx.Expr[T](ctx.typecheck(ctx.untypecheck(e.tree)))

    // necessary because parsed string wasn't typed yet
    retypeCheck(reify(ExpressionAndSyntax(value.splice, code.splice)))
  }
}