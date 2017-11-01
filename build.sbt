scalaVersion := "2.11.8"

version := "1.0"

mainClass in (Compile, packageBin) := Some("Main")
mainClass in (Compile, run) := Some("Main")

libraryDependencies ++= Seq(
	// "ch.qos.logback"          %  "logback-classic" % "1.2.3",
	"com.typesafe.play" % "play-json_2.11" % "2.6.7",
	"com.typesafe.play" % "play-functional_2.11" % "2.6.7",
	"com.typesafe.play" % "play-ws_2.11" % "2.6.6",
	"com.typesafe.akka" % "akka-actor_2.11" % "2.5.6",
    "com.typesafe.akka" % "akka-http_2.11" % "10.0.10",
    "com.typesafe.akka" % "akka-http-core_2.11" % "10.0.10"
)
