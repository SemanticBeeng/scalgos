name := "scalgos"
version := "1.0-SNAPSHOT"
description := "Algorithms in Scala"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
organization := "com.github.pathikrit"

scalaVersion := "2.11.8"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_")

javacOptions ++= Seq("-encoding", "UTF-8")

libraryDependencies ++= Seq(
  //"org.specs2"      %% "specs2-core"    % "3.7" % Test
  "org.specs2" % "specs2-core_2.11" % "3.8.4-20160711064123-77be371" % Test
)
