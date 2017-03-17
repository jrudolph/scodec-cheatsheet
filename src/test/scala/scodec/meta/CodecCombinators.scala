package scodec.meta

import shapeless.HNil

import scodec.bits._
import CodecInfo.info
import scodec.codecs._
import ExpressionAndSyntax.AddWithSyntax

trait CodecCombinators {
  val CodecCombinators = Seq[CodecInfo[_]](
    // Note, explicit `withSyntax` is needed for right-associative operators, otherwise implicit conversion will be added at the wrong place in the expression
    info(uint8.hlist, "Convert codec to HList", 42 :: HNil withSyntax),

    info(uint8 pairedWith uint16, "Concat two codecs into a 2-tuple", (0x12, 0x3456)),
    info(uint8 pairedWith uint16 pairedWith cstring, "Concat two codecs into a 2-tuple", ((0x12, 0x3456), "abc")),
    info(uint8 ~ uint16, "Concat two codecs into a 2-tuple", (0x12, 0x3456)),
    info(uint8 ~ uint16 ~ cstring, "Concat two codecs into a 2-tuple", ((0x12, 0x3456), "abc")),

    info(constant(0x12) dropLeft uint8, "Concat two codecs discarding the Unit value of the first one", 0x68),
    info(constant(0x12) ~> uint8, "Concat two codecs discarding the Unit value of the first one", 0x68),

    info(uint8 flatZip { case i if i < 100 ⇒ uint8; case _ ⇒ uint16 }, "Concat two codecs into a 2-tuple where the second codec is choosen depending on the result of the first.", (23, 42), (112, 0x1234)),
    info(uint8 >>~ { case i if i < 100 ⇒ uint8; case _ ⇒ uint16 }, "Concat two codecs into a 2-tuple where the second codec is choosen depending on the result of the first.", (23, 42), (112, 0x1234)),

    info(uint8 :: cstring, "Concat two codecs into an HList", 0x42 :: "zweiundvierzig" :: HNil withSyntax),
    info(CodecMetadata.meta((constant(hex"ca fe") :: cstring).dropUnits), "Remove Unit elements from an HList codec", "dreiundzwanzig" :: HNil withSyntax),
    info(
      CodecMetadata.meta((uint8 :: cstring) ::: (constant(0x23) :: uint16)),
      "Concat two HList codecs", 0x42 :: "zweiundvierzig" :: () :: 0xabcd :: HNil withSyntax)
  )

  /* combinator TODO:
xmap
exmap

narrow
widen

dropRight
<~

flattenLeftPairs

unit

consume

complete
compact

upcast
downcast
withContext
|

:+:
toField

::
:~>:
dropUnits
:+
:::
flatConcat
flatAppend
polyxmap
polyxmap1
derive

::
:~>:
flatPrepend
>>:~
flatZipHList
   */
}
