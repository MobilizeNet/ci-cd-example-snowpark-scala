scalaVersion := "2.12.15"
name := "ci-cd-sample"
organization := "com.mobilize"
version := s"1.0-${sys.env("GITHUB_REF_NAME")}"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"
libraryDependencies += "com.snowflake" % "snowpark" % "1.2.0" 
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test

libraryDependencies += "org.apache.pdfbox" % "pdfbox-app" % "2.0.27" 

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

/* When creating the uber-jar we do not need the snowpark libs */
assembly / assemblyExcludedJars := {
    val cp = (assembly / fullClasspath).value
    cp filter { f =>
      f.data.getName.contains("snowpark") ||
       f.data.getName.contains("snowflake")
    }
  }
