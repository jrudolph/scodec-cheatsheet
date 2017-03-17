import sbt.Keys.scalacOptions

val scodecV = "1.10.3"
val macroParadiseV = "2.1.0"
val specs2V = "3.8.9"

val scodecCore = "org.scodec" %% "scodec-core" % scodecV

def commonSettings = Seq(
  scalaVersion := "2.12.1",

  scalacOptions ++=
    Seq(
      "-Yrangepos", // needed for macros to extract code
      "-deprecation", "-feature", "-encoding", "utf8", "-Ywarn-dead-code", "-unchecked", "-Xlint", "-Ywarn-unused-import")
)

lazy val root =
  project.in(file("."))
    .aggregate(macros, metadata, generator)

lazy val macros =
  project.in(file("macros"))
    .settings(commonSettings: _*)
    .settings(
      addCompilerPlugin("org.scalamacros" % "paradise" % macroParadiseV cross CrossVersion.full),
      libraryDependencies ++= Seq(
        scodecCore,
        "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided"
      )
    )

lazy val metadata =
  project.in(file("metadata"))
    .dependsOn(macros)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        scodecCore,
        "org.specs2" %% "specs2-core" % specs2V % "test"
      )
    )

lazy val generator =
  project.in(file("generator"))
    .settings(commonSettings: _*)
    .dependsOn(metadata)
