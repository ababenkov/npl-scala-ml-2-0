ThisBuild / resolvers ++= Seq(
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
    Resolver.mavenLocal
)

name := "flink-rnn"

version := "0.1-SNAPSHOT"

organization := "org.example"

ThisBuild / scalaVersion := "2.11.12"

val flinkVersion = "1.9.1"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion
)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )

libraryDependencies += "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-beta5"

libraryDependencies += "org.nd4j" % "nd4j-native-platform" % "1.0.0-beta5"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "maven", x,  _*) if x.startsWith("org.nd4j") => MergeStrategy.first
  case PathList("META-INF", "services", x, _*) if x.startsWith("org.nd4j") => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.last
}



libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.10.0",
  "io.circe" %% "circe-generic" % "0.10.0",
  "io.circe" %% "circe-parser" % "0.10.0",
  "org.slf4j" % "slf4j-log4j12" % "1.7.7"
)

assembly / mainClass := Some("org.example.LSTMStreamingApp")

// make run command include the provided dependencies
Compile / run  := Defaults.runTask(Compile / fullClasspath,
                                   Compile / run / mainClass,
                                   Compile / run / runner
                                  ).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)
