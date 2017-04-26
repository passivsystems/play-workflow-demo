name := "play-workflow-example"

organization := "com.passivsystems"

scalaVersion := "2.11.7"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  //"com.github.passivsystems" % "play-workflow" % "0.1.1"
  "com.passivsystems" %% "play-workflow" % "0.1.1-SNAPSHOT"
)

routesGenerator := StaticRoutesGenerator

enablePlugins(PlayScala)
