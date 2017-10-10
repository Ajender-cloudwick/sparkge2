import sun.security.tools.PathList

name := "sparkge2"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.2.0",
  "org.apache.spark" %% "spark-streaming" % "2.2.0",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.2.0"
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1200-jdbc41"
libraryDependencies += "org.skife.com.typesafe.config" % "typesafe-config" % "0.3.0"