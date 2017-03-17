package scodec.meta

import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.macros.blackbox

case class ExpressionAndSyntax[T](value: T, syntax: String)
object ExpressionAndSyntax {
  implicit def withSyntax[T](value: T): ExpressionAndSyntax[T] = macro exprMacro[T]
  def fromString[T](code: String): ExpressionAndSyntax[T] = macro exprFromStringMacro[T]

  def exprMacro[T: ctx.WeakTypeTag](ctx: blackbox.Context)(value: ctx.Expr[T]): ctx.Expr[ExpressionAndSyntax[T]] = {
    import ctx.universe._

    val pos = value.tree.pos
    if (!pos.isRange) println(s"Does not have a rangePos $pos $value")

    val str = new String(pos.source.content.slice(pos.start, pos.end))

    // TODO: DRY up with CodecMetadata
    def lit(str: String): ctx.Expr[String] = ctx.Expr[String](q"$str")

    //println(s"""Prefix: ${ctx.prefix.tree} ${ctx.macroApplication} Pos: $pos Value: ${value.tree} Text: '$str'""")

    reify(ExpressionAndSyntax(value.splice, lit(str).splice))
  }
  def exprFromStringMacro[T: ctx.WeakTypeTag](ctx: blackbox.Context)(code: ctx.Expr[String]): ctx.Expr[ExpressionAndSyntax[T]] = {
    import ctx.universe._
    val codeString = code.tree match {
      case Literal(Constant(str: String)) ⇒ str
    }
    val value = ctx.Expr(ctx.parse(codeString))

    def retypeCheck[X](e: ctx.Expr[X]): ctx.Expr[X] = ctx.Expr[X](ctx.typecheck(ctx.untypecheck(e.tree)))

    // necessary because parsed string wasn't typed yet
    retypeCheck(reify(ExpressionAndSyntax(value.splice, code.splice)))
  }
}