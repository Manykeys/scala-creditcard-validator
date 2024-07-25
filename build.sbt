name := "scala-creditcard-validator"
version := "0.1"
scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.19",
  "com.typesafe.akka" %% "akka-stream" % "2.6.19",
  "com.typesafe.akka" %% "akka-http" % "10.2.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.9",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.2.9" % Test,
  "org.scalatest" %% "scalatest" % "3.2.13" % Test
)
