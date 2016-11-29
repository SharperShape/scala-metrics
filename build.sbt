name := "scala-metrics"

organization := "com.sharpershape"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation")

testOptions in Test := Nil

parallelExecution in Test := false

fork in Test := true

coverageMinimum := 95
    
coverageFailOnMinimum := true

scalastyleConfigUrl in Compile := sharperShapeScalastyleConfigUrl

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion % "provided"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
