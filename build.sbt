name := "scala-metrics"

organization := "com.sharpershape"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation")

testOptions in Test := Nil

parallelExecution in Test := false

fork in Test := true

coverageMinimum := 95
    
coverageFailOnMinimum := true

scalastyleConfigUrl in Compile := Some(new URL("https://raw.githubusercontent.com/SharperShape/code-styles/master/scalastyle-config.xml"))

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % sys.props.getOrElse("akka.version", default = "2.4.9") % "provided"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test",
  "com.typesafe.akka" %% "akka-testkit" % sys.props.getOrElse("akka.version", default = "2.4.9") % "test"
)
