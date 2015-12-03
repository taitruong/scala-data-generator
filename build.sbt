name := "bi-data-generator"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.0",
  //use ActiveSlick for generic DAOs
  "io.strongtyped" %% "active-slick" % "0.3.3",

  "org.postgresql" % "postgresql" % "9.4-1204-jdbc41",
  //for hikari: do not use HikariCP (only for Java 8) instead use HikariCP-java6
  //it also supports JDK7 otherwise the is an unsupported version error
  //"com.zaxxer" % "HikariCP-java6" % "2.3.12",
  "com.zaxxer" % "HikariCP" % "2.4.2",
  "com.typesafe.slick" % "slick-hikaricp_2.11" % "3.1.0",

  "ch.qos.logback" % "logback-classic" % "1.1.3",

  "org.scalatest" %% "scalatest" % "2.2.4" % "test",

  //Gaussian libraries
  // other dependencies here
  "org.scalanlp" %% "breeze" % "0.10",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes.
  "org.scalanlp" %% "breeze-natives" % "0.10"
)

slick <<= slickCodeGenTask

sourceGenerators in Compile <+= slickCodeGenTask

lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "main/slick").getPath
  val username = "postgres"
  val password = "postgres"
  val url = "jdbc:postgresql://127.0.0.1/Artificial"
  val jdbcDriver = "org.postgresql.Driver"
  val slickDriver = "slick.driver.PostgresDriver"
  val pkg = "bi.models"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, username, password), s.log))
  val fname = outputDir + "/" + "bi/models" + "/Tables.scala"
  Seq(file(fname))
}

resolvers ++= Seq(
  // other resolvers here
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)