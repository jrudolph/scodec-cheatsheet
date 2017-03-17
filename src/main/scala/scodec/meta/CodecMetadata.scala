package scodec.meta

import scala.annotation.tailrec
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

import shapeless.HNil

import scodec.Codec

case class CodecMetadata[T](codec: Codec[T], name: String, syntax: String, tpeString: String)
object CodecMetadata {
  implicit def meta[T](codec: Codec[T]): CodecMetadata[T] = macro metadataMacro[T]

  def metadataMacro[T: ctx.WeakTypeTag](ctx: blackbox.Context)(codec: ctx.Expr[Codec[T]]): ctx.Expr[CodecMetadata[T]] = {
    import ctx.universe._

    val tree = codec.tree

    @tailrec
    def getName(tree: Tree): String = tree match {
      case q"$qual.${ name: TermName }" ⇒ name.decodedName.toString
      case q"$qual.${ name: TermName }[..${ targs }](...${ args })" ⇒ name.decodedName.toString
      case Block(_, expr) ⇒ getName(expr)
    }
    val name = getName(tree)

    val syntax = {
      val pos = tree.pos
      if (!pos.isRange) println(s"Does not have a rangePos $pos $tree")

      new String(pos.source.content.drop(pos.start).take(pos.end - pos.start))
    }

    val elementTpe = weakTypeOf[T].dealias.map(_.dealias)

    val ShapelessColonSymbol = weakTypeOf[shapeless.::[Int, HNil]].typeConstructor.typeSymbol
    val HNilType = weakTypeOf[shapeless.HNil]

    def simplifyType(tpe: Type): String = tpe match {
      case HNilType                                 ⇒ "HNil"
      case TypeRef(_, `ShapelessColonSymbol`, args) ⇒ args.map(simplifyType).map(_.toString).mkString(" :: ")
      case x ⇒
        //println(s"Type is $tpe class: ${tpe.getClass}")
        x.toString
    }
    val tpe: String = simplifyType(elementTpe) //.toString //typeSymbol.name.toString

    reify(CodecMetadata(codec.splice, ctx.literal(name).splice, ctx.literal(syntax).splice, ctx.literal(tpe).splice))
  }
}
