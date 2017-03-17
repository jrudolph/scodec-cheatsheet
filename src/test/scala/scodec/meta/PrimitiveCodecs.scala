package scodec.meta

import java.util.UUID

import scodec.bits._
import scodec.codecs
import CodecInfo.info

trait PrimitiveCodecs {
  val PrimitiveCodecs = Seq[CodecInfo[_]](
    info(codecs.bits, "All the remaining bits", ExpressionAndSyntax.fromString("""bin"11010110101"""")),
    info(codecs.bytes, "All the remaining bytes", ExpressionAndSyntax.fromString("""hex"5468697320697320"""")),

    info(codecs.byte, "A single byte", 89: Byte),

    info(codecs.ushort8, "8-bit unsigned short", 240: Short),
    info(codecs.short16, "16-bit big-endian short", 0x1234: Short),

    info(codecs.int8, "8-bit integer", 0x12),
    info(codecs.int16, "16-bit big-endian integer", 0x1234),
    info(codecs.int24, "24-bit big-endian integer", 0x123456),
    info(codecs.int32, "32-bit big-endian integer", 0x12345678),
    info(codecs.int64, "64-bit big-endian integer", 0x12345678abcdefL),

    info(codecs.uint2, "2-bit unsigned integer", 0x3),
    info(codecs.uint4, "4-bit unsigned integer", 0x6),
    info(codecs.uint8, "8-bit unsigned integer", 0xad),
    info(codecs.uint16, "16-bit big-endian unsigned integer", 0xadac),
    info(codecs.uint32, "32-bit big-endian unsigned integer", 0x81234567L),

    info(codecs.int8L, "8-bit little-endian integer", 0x12),
    info(codecs.int16L, "16-bit little-endian integer", 0x1234),
    info(codecs.int24L, "24-bit little-endian integer", 0x123456),
    info(codecs.int32L, "32-bit little-endian integer", 0x12345678),
    info(codecs.int64L, "64-bit little-endian integer", 0x12345678abcdefL),

    info(codecs.uint2L, "2-bit little-endian unsigned integer", 0x3),
    info(codecs.uint4L, "4-bit little-endian unsigned integer", 0x6),
    info(codecs.uint8L, "8-bit little-endian unsigned integer", 0xad),
    info(codecs.uint16L, "16-bit little-endian unsigned integer", 0xadac),
    info(codecs.uint32L, "32-bit little-endian unsigned integer", 0x81234567L),

    info(codecs.vint, "Variable-length big-endian integer", 0x01, 0x123, 0x1234, 0x12345, 0x123456, 0x1234567, 0x12345678),
    info(codecs.vintL, "Variable-length little-endian integer", 0x123456),
    info(codecs.vlong, "Variable-length big-endian long", 0x81234567abcdL),
    info(codecs.vlongL, "Variable-length little-endian long", 0x81234567abcdL),
    info(codecs.vpbcd, "Variable-length packed decimal longs", 1L, 123L, 12345L, 12345678901L),

    info(codecs.float, "32-bit big-endian IEEE 754 floating point number", 1.234234387f),
    info(codecs.floatL, "32-bit little-endian IEEE 754 floating point number", 1.234234387f),
    info(codecs.double, "64-bit big-endian IEEE 754 floating point number", 1.234234387d),
    info(codecs.doubleL, "64-bit little-endian IEEE 754 floating point number", 1.234234387d),
    info(codecs.bool, "1-bit boolean", true),

    info(codecs.ascii, "`US-ASCII` String consuming remaining input", "The lazy dog"),
    info(codecs.utf8, "`UTF8` String consuming remaining input", "árvíztűrő ütvefúrógép"),
    info(codecs.cstring, "Null-terminated `US-ASCII` String", "The lazy dog"),

    info(codecs.ascii32, "`US-ASCII` String prefixed by 32-bit 2s complement big-endian size field", "The lazy dog"),
    info(codecs.ascii32L, "`US-ASCII` String prefixed by 32-bit 2s complement little-endian size field", "The lazy dog"),
    info(codecs.utf8_32, "`UTF8` String prefixed by 32-bit 2s complement big-endian size field", "árvíztűrő ütvefúrógép"),
    info(codecs.utf8_32L, "`UTF8` String prefixed by 32-bit 2s complement little-endian size field", "árvíztűrő ütvefúrógép"),

    info(codecs.uuid, "`UUID` as 2 64-bit big-endian longs", UUID.fromString("f41561aa-f759-4cde-ab07-c0c0cc0db8d8"))
  )
}
