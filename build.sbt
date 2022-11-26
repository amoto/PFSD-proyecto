
ThisBuild / name := "2022-PFSD-Proyecto-JDevia"

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.13.10"

ThisBuild / libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.3.1"
ThisBuild / libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "3.3.1"
ThisBuild / libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
ThisBuild / libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
ThisBuild / libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.0"
ThisBuild / libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.1"
ThisBuild / libraryDependencies += "org.apache.commons" % "commons-dbcp2" % "2.9.0"
ThisBuild / libraryDependencies += "org.postgresql" % "postgresql" % "42.5.0"

ThisBuild / Test / fork := true

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}


