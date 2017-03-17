package scodec.meta

case class CodecInfo[T](
    metadata:    CodecMetadata[T],
    description: String,
    examples:    Seq[ExpressionAndSyntax[T]]
) {
  def exampleString(ex: ExpressionAndSyntax[T]): String = ex.syntax
  def exampleEncoding(tExpr: ExpressionAndSyntax[T]): String = {
    val bits = metadata.codec.encode(tExpr.value).require
    if (bits.size % 8 == 0) {
      val bytes = bits.toByteVector
      s"`${bytes.toIterable.map(_ formatted "%02x").mkString(" ")}₁₆` (${bytes.size.plural("byte")})"
    } else
      s"`${bits.toBin.grouped(8).mkString(" ")}₂` (${bits.size.plural("bit")})"
  }

  def exampleMapping(ex: ExpressionAndSyntax[T]): String = {
    s"`${exampleString(ex)}` ⇔ ${exampleEncoding(ex)}"
  }

  def exampleStrings: Seq[String] = examples.map(exampleMapping)
}

object CodecInfo {
  def info[T](metadata: CodecMetadata[T], description: String, examples: ExpressionAndSyntax[T]*): CodecInfo[T] =
    CodecInfo(metadata, description, examples)
}