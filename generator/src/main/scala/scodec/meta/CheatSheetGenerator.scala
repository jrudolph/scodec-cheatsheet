package scodec.meta

import java.io.File
import java.io.FileOutputStream

import scala.io.Source

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

  val tablesString =
    s"""
       %## Primitive Codecs
       %
       %${createTable(ScodecCodecs.PrimitiveCodecs, Columns.PrimitiveColumns)}
       %
       %## Parameterized Codecs
       %
       %${createTable(ScodecCodecs.ParameterizedCodecs, Columns.ComplexColumns)}
       %
       %## Combinators
       %
       %${createTable(ScodecCodecs.CodecCombinators, Columns.ComplexColumns)}
       %
       %""".stripMargin('%')

  def patchREADME(newTable: String) = {
    val readmeFile = new File("README.md")
    require(readmeFile.exists(), s"no README.md found at ${readmeFile.getCanonicalPath}")
    println(s"Updaing ${readmeFile.getCanonicalPath}")

    val newFile = File.createTempFile("readme", ".md.tmp")

    val prefix =
      Source.fromFile(readmeFile, "utf8").getLines()
        .takeWhile(!_.startsWith("## Primitive Codecs"))
        .mkString("\n")

    val suffix =
      Source.fromFile(readmeFile, "utf8").getLines()
        .dropWhile(!_.startsWith("## Contributing"))
        .mkString("\n")

    val fos = new FileOutputStream(newFile)
    fos.write((prefix + tablesString + suffix).getBytes("utf8"))
    fos.close()
    newFile.renameTo(readmeFile)
  }

  patchREADME(tablesString)
}
