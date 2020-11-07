name := "rest-api-slurper"
version := "1.0"
scalaVersion := "2.13.1"

val _mainClass = "com.rohith.etl.Apicall"
mainClass := Some(_mainClass)


val http4sVersion = "0.21.2"
val circeVersion = "0.13.0"
val catsRetryVersion = "0.3.0"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime,
  "org.log4s" %% "log4s" % "1.8.2",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "com.github.cb372" %% "cats-retry-core"        % catsRetryVersion,
  "com.github.cb372" %% "cats-retry-cats-effect" % catsRetryVersion,
  "software.amazon.awssdk" % "s3" % "2.7.34",
  "org.apache.hadoop" % "hadoop-common" % "3.2.1",
  "org.apache.hadoop" % "hadoop-client" % "2.6.0",
  "org.apache.hadoop" % "hadoop-aws" % "3.2.1",
  "com.github.mjakubowski84" %% "parquet4s-core" % "1.0.0"
)

// Required for Cats effects
scalacOptions ++= Seq(
  "-Ymacro-annotations",
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds")

 mainClass in assembly := Some(_mainClass)

// Required for awssdk
assemblyMergeStrategy in assembly := {
  case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
