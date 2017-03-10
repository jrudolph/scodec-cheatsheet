package scodec

import scodec.bits._
import codecs._

object CheatSheetGenerator extends App {
  case class CodecInfo[T](
      metadata:    CodecMetadata[T],
      description: String,
      example:     Option[ExpressionAndSyntax[T]]
  ) {
    def exampleString: String = example.map(_.syntax).getOrElse("")
    def exampleEncoding: String = example.map { tExpr ⇒
      val bits = metadata.codec.encode(tExpr.value).require
      if (bits.size % 8 == 0) {
        val bytes = bits.toByteVector
        s"`${bytes.toIterable.map(_ formatted "%02x").mkString(" ")}₁₆` (${bytes.size.plural("byte")})"
      } else
        s"`${bits.toBin}₂` (${bits.size.plural("bit")})"
    }.getOrElse("")
  }
  implicit class Pluralizer(val num: Long) extends AnyVal {
    def plural(what: String): String = {
      val s = if (num > 1) "s" else ""
      s"$num $what$s"
    }

    def bitsOrBytes: String = {
      num.plural("bit") + {
        if (num % 8 == 0) s" (${(num / 8).plural("byte")})"
        else ""
      }
    }
  }

  def info[T](metadata: CodecMetadata[T], description: String, example: ExpressionAndSyntax[T]): CodecInfo[T] =
    CodecInfo(metadata, description, Some(example))

  def info[T](metadata: CodecMetadata[T], description: String): CodecInfo[T] =
    CodecInfo(metadata, description, None)

  val primitives = Seq(
    info(codecs.bits, "All the remaining bits", bin"11010110101"),
    info(codecs.bytes, "All the remaining bytes", hex"5468697320697320"),

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

    info(codecs.vint, "Variable-length big-endian integer", 0x8123456),
    info(codecs.vintL, "Variable-length little-endian integer", 0x123456),
    info(codecs.vlong, "Variable-length big-endian long", 0x81234567abcdL),
    info(codecs.vlongL, "Variable-length little-endian long", 0x81234567abcdL),
    info(codecs.vpbcd, "Variable-length packed decimal longs", 12345678901L)
  )

  /*
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
   */

  def createTable(infos: Seq[CodecInfo[_]]): String = {
    case class Column(name: String, extractor: CodecInfo[_] ⇒ String)
    def bqColumn(name: String, extractor: CodecInfo[_] ⇒ String): Column =
      Column(name, extractor.andThen(str ⇒ s"`$str`"))

    val columns = Seq(
      Column("Name", _.metadata.name),
      bqColumn("Element Type", _.metadata.tpeString),
      Column("Description", _.description),
      Column("Min Bits", _.metadata.codec.sizeBound.lowerBound.toString),
      Column("Max Bits", _.metadata.codec.sizeBound.upperBound.map(_.toString).getOrElse("∞")),
      bqColumn("Example Value", _.exampleString),
      Column("Example Encoding", _.exampleEncoding)
    )

    def formatRow(cells: Seq[String]): String = s"| ${cells.mkString(" | ")} |"

    val rows =
      infos.map { i ⇒
        columns.map(_.extractor(i))
      }

    formatRow(columns.map(_.name)) + "\n" + // headers
      formatRow(columns.map(_ ⇒ "-")) + "\n" + // separator line
      rows.map(formatRow).mkString("\n")
  }

  println(createTable(primitives))
}
