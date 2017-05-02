name := "play-workflow-demo"

organization := "com.passivsystems"

scalaVersion := "2.11.7"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.github.passivsystems.play-workflow" %% "play-workflow" % "0.3.0"
)

enablePlugins(PlayScala)
