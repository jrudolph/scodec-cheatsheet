package scodec.meta

object CheatSheetGenerator extends App {
  case class Column(name: String, extractor: CodecInfo[_] ⇒ String) {
    def named(newName: String) = copy(name = newName)
  }
  def bqColumn(name: String, extractor: CodecInfo[_] ⇒ String): Column =
    Column(name, extractor.andThen(str ⇒ s"`$str`"))

  def examples(info: CodecInfo[_]): String =
    info.exampleStrings.mkString(" </br> ")

  object Columns {
    val Name = bqColumn("Name", _.metadata.name)
    val Code = bqColumn("Example usage", _.metadata.syntax)
    val ElementType = bqColumn("Result Type", _.metadata.tpeString)
    val Description = Column("Description", _.description)
    val MinBits = Column("Min Bits", _.metadata.codec.sizeBound.lowerBound.toString)
    val MaxBits = Column("Max Bits", _.metadata.codec.sizeBound.upperBound.map(_.toString).getOrElse("∞"))
    val Examples = Column("Coding Examples", examples)

    val PrimitiveColumns = Seq(
      Name, ElementType, Description, MinBits, MaxBits, Examples
    )

    val ComplexColumns = Seq(
      Name, Description, Code,
      ElementType.named("Example result type"),
      MinBits.named("Example Min Bits"),
      MaxBits.named("Example Max Bits"),
      Examples
    )
  }

  def createTable(infos: Seq[CodecInfo[_]], columns: Seq[Column]): String = {
    def formatRow(cells: Seq[String]): String = s"| ${cells.mkString(" | ")} |"

    val rows =
      infos.map { i ⇒
        columns.map(_.extractor(i))
      }

    formatRow(columns.map(_.name)) + "\n" + // headers
      formatRow(columns.map(_ ⇒ "-")) + "\n" + // separator line
      rows.map(formatRow).mkString("\n")
  }

  println("## Primitive Codecs")
  println()
  println(createTable(ScodecCodecs.PrimitiveCodecs, Columns.PrimitiveColumns))
  println()
  println("## Parametrized Codecs")
  println()
  println(createTable(ScodecCodecs.ParameterizedCodecs, Columns.ComplexColumns))
  println()
  println("## Combinators")
  println()
  println(createTable(ScodecCodecs.CodecCombinators, Columns.ComplexColumns))
}
