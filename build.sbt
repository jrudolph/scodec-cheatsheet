addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "org.scodec" %% "scodec-core" % "1.10.3",
  "org.specs2" %% "specs2-core" % "3.8.9" % "test"
)

libraryDependencies :=
 libraryDependencies.value :+
  "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided"

scalaVersion := "2.12.1"

// to support ExpressionAndSyntax macro
scalacOptions in Test += "-Yrangepos"