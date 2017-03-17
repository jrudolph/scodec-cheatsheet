package scodec.meta

import scodec.bits._
import scodec.codecs
import CodecInfo.info

trait ParameterizedCodecs {
  val ParameterizedCodecs = Seq[CodecInfo[_]](
    info(codecs.constant(bin"110101"), "Constant bits", ()),
    info(codecs.constant(bin"10"), "Constant bits", ()),

    info(codecs.constant(hex"1234567890"), "Constant bytes", ()),
    info(codecs.constant(hex"abcdef"), "Constant bytes", ()),

    info(codecs.constant(0x42: Byte), "Constant bytes", ()),
    info(codecs.constant(0x2342: Short, 0x42: Short), "Constant bytes from integral", ()),
    info(codecs.constant(0x2342: Int, 0x42: Short), "Constant bytes from integral", ()),
    info(codecs.constant(0x2342: Long, 0x42: Short), "Constant bytes from integral", ())
  )

  /*
TODO:

`bits(size)`
`bytes(size)`

`byte(nBits)`
`ubyte(nBits)`
`short(nBits)`
`ushort(nBits)`
`int(nBits)`
`uint(nBits)`
`long(nBits)`
`ulong(nBits)`
`shortL(nBits)`
`ushortL(nBits)
`intL(nBits)`
`uintL(nBits)`
`longL(nBits)`
`ulongL(nBits)`

bool(nBits)
string(charset)

string32(charset)
string32L(charset)

provide
ignore

3 variants of constantLenient

fixedSizeBits
fixedSizeBytes

paddedFixedSizeBits
paddedFixedSizeBitsDependent

limitedSizeBits
limitedSizeBytes

variableSizeBits
variableSizeBytes

variableSizeBitsLong
variableSizeBytesLong

variableSizePrefixedBits
variableSizePrefixedBytes

variableSizePrefixedBitsLong
variableSizePrefixedBytesLong

peek
peekVariableSizeBits
peekVariableSizeBitsLong
peekVariableSizeBytes
peekVariableSizeBytesLong

byteAligned
conditional
optional

bitsRemaining

withDefault

withDefaultValue

recover

lookahead

choice

vector
vectorOfN
sizedVector
vectorMultiplexed
vectorDelimited

list
listOfN
sizedList
listMultiplexed
listDelimited

endiannessDependent

either
fallback
lazily
fail

zlib

filtered

checksummed

encrypted

fixedSizeSignature
variableSizeSignature

certificate

x509Certificate

discriminated.by

mappedEnum

discriminatorFallback

enumerated

hlist

log...

constrainedVariableSizeBytes
constrainedVariableSizeBytesLong
constrainedVariableSizeBytes
constrainedVariableSizeBytesLong

 */
}
