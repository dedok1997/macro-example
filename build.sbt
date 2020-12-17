name := "macro-example"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

scalacOptions ++= Seq(
  "-Ymacro-annotations"
  , "-language:experimental.macros"
)