name := "templates"

version := "1.0"

scalaVersion := "2.11.8"

val json4sVersion = "3.3.0"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.json4s" %% "json4s-native" % json4sVersion
)

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

resolvers += "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

resolvers += "Central Maven" at "http://central.maven.org/maven2/"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

