package scodec.meta

import shapeless._
import scodec.bits._
import CodecInfo.info
import scodec.codecs._
import CodecMetadata.meta
import ExpressionAndSyntax.withSyntax
import scodec.Codec
import scodec.meta.CodecInfo.info

trait CodecCombinators {
  val CodecCombinators = Seq[CodecInfo[_]](
    // Note, explicit `withSyntax` and `meta` is needed for right-associative operators,
    // implicit conversion does not work because it will be added at the wrong place in the expression.
    info(uint8.hlist, "Convert codec to HList", withSyntax(42 :: HNil)),

    info(uint8 pairedWith uint16, "Concat two codecs into a 2-tuple", (0x12, 0x3456)),
    info(uint8 pairedWith uint16 pairedWith cstring, "Concat two codecs into a 2-tuple", ((0x12, 0x3456), "abc")),
    info(uint8 ~ uint16, "Concat two codecs into a 2-tuple", (0x12, 0x3456)),
    info(uint8 ~ uint16 ~ cstring, "Concat two codecs into a 2-tuple", ((0x12, 0x3456), "abc")),

    info(uint8 ~ uint16 ~ cstring flattenLeftPairs, "Removes left tuple nesting and converts to HList", withSyntax(0x12 :: 0x3456 :: "abc" :: HNil)),

    info(constant(0x12) dropLeft uint8, "Concat two codecs discarding the Unit value of the left one", 0x68),
    info(constant(0x12) ~> uint8, "Concat two codecs discarding the Unit value of the left one", 0x68),

    info(uint8 dropRight constant(0x12), "Concat two codecs discarding the Unit value of the right one", 0x68),
    info(uint8 <~ constant(0x12), "Concat two codecs discarding the Unit value of the right one", 0x68),

    info(uint8 flatZip { case i if i < 100 ⇒ uint8; case _ ⇒ uint16 }, "Concat two codecs into a 2-tuple where the second codec is choosen depending on the result of the first.", (23, 42), (112, 0x1234)),
    info(uint8 >>~ { case i if i < 100 ⇒ uint8; case _ ⇒ uint16 }, "Concat two codecs into a 2-tuple where the second codec is choosen depending on the result of the first.", (23, 42), (112, 0x1234)),

    info(
      byteAligned(bool).consume[Int](isLarge ⇒ if (isLarge) uint16 else uint8)(_ > 0xff),
      "Decodes the left codec and then the right codec depending on the left result, then discards the left value and returns only the right",
      255, 256, 0x1234
    ),

    info(meta(uint8 :: cstring), "Concat two codecs into an HList", withSyntax(0x42 :: "zweiundvierzig" :: HNil)),
    info(meta((constant(hex"ca fe") :: cstring).dropUnits), "Remove Unit elements from an HList codec", withSyntax("dreiundzwanzig" :: HNil)),

    info(
      meta(constant(hex"ab cd ef") :~>: uint16 :: uint8),
      "Concat two codecs discarding the Unit value of the left one",
      withSyntax(0x1234 :: 0x23 :: HNil)
    ),

    info(
      meta(uint8.hlist :+ uint16),
      "Append to left HList codec the right codec.",
      withSyntax(0x42 :: 0xcdef :: HNil)
    ),

    info(
      meta((uint8 :: cstring) ::: (constant(0x23) :: uint16)),
      "Concat two HList codecs", withSyntax(0x42 :: "zweiundvierzig" :: () :: 0xabcd :: HNil)),

    info(
      meta(uint8 flatZipHList { case strSize ⇒ limitedSizeBytes(strSize, utf8) }),
      "Concat to HList result of first Codec with result of Codec determined after based on value of first part",
      withSyntax(8 :: "sausages" :: HNil)
    ),

    info(
      meta(uint8 flatPrepend { case strSize ⇒ limitedSizeBytes(strSize, utf8) :: uint16 }),
      "Concat to HList result of first Codec with result of HList Codec determined after based on value of first part",
      withSyntax(8 :: "sausages" :: 0x1234 :: HNil)
    ),

    info(
      meta(uint8 >>:~ { case strSize ⇒ limitedSizeBytes(strSize, utf8) :: uint16 }),
      "Concat to HList result of first Codec with result of HList Codec determined after based on value of first part",
      withSyntax(8 :: "sausages" :: 0x1234 :: HNil)
    ),

    info(
      meta(uint8 :: uint16 flatAppend { case strSize :: _ :: HNil ⇒ limitedSizeBytes(strSize, utf8) }),
      "Concat result of first HList Codec with result of Codec determined after based on value of first part",
      withSyntax(8 :: 0xabcd :: "sausages" :: HNil)
    ),

    info(
      meta(uint8 :: uint16 flatConcat { case strSize :: _ :: HNil ⇒ limitedSizeBytes(strSize, utf8) :: uint16 }),
      "Concat result of first HList Codec with result of HList Codec determined after based on value of first part",
      withSyntax(8 :: 0xabcd :: "sausages" :: 0x1234 :: HNil)
    )
  )

  /* combinator TODO:
xmap
exmap

narrow
widen

unit

complete
compact

upcast
downcast
withContext
|

:+:
toField

polyxmap
polyxmap1
derive

   */
}
