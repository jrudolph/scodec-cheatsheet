package scodec.meta

import shapeless.HNil

import scodec.bits._
import CodecInfo.info
import scodec.codecs._
import CodecMetadata.meta
import ExpressionAndSyntax.withSyntax

trait CodecCombinators {
  val CodecCombinators = Seq[CodecInfo[_]](
    // Note, explicit `withSyntax` and `meta` is needed for right-associative operators,
    // implicit conversion does not work because it will be added at the wrong place in the expression.
    info(uint8.hlist, "Convert codec to HList", withSyntax(42 :: HNil)),

    info(uint8 pairedWith uint16, "Concat two codecs into a 2-tuple", (0x12, 0x3456)),
    info(uint8 pairedWith uint16 pairedWith cstring, "Concat two codecs into a 2-tuple", ((0x12, 0x3456), "abc")),
    info(uint8 ~ uint16, "Concat two codecs into a 2-tuple", (0x12, 0x3456)),
    info(uint8 ~ uint16 ~ cstring, "Concat two codecs into a 2-tuple", ((0x12, 0x3456), "abc")),

    info(constant(0x12) dropLeft uint8, "Concat two codecs discarding the Unit value of the first one", 0x68),
    info(constant(0x12) ~> uint8, "Concat two codecs discarding the Unit value of the first one", 0x68),

    info(uint8 flatZip { case i if i < 100 ⇒ uint8; case _ ⇒ uint16 }, "Concat two codecs into a 2-tuple where the second codec is choosen depending on the result of the first.", (23, 42), (112, 0x1234)),
    info(uint8 >>~ { case i if i < 100 ⇒ uint8; case _ ⇒ uint16 }, "Concat two codecs into a 2-tuple where the second codec is choosen depending on the result of the first.", (23, 42), (112, 0x1234)),

    info(meta(uint8 :: cstring), "Concat two codecs into an HList", withSyntax(0x42 :: "zweiundvierzig" :: HNil)),
    info(meta((constant(hex"ca fe") :: cstring).dropUnits), "Remove Unit elements from an HList codec", withSyntax("dreiundzwanzig" :: HNil)),
    info(
      meta((uint8 :: cstring) ::: (constant(0x23) :: uint16)),
      "Concat two HList codecs", withSyntax(0x42 :: "zweiundvierzig" :: () :: 0xabcd :: HNil))
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
