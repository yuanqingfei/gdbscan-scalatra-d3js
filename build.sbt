import org.scalatra.sbt.ScalatraPlugin
import com.earldouglas.xwp.JettyPlugin
import sbt.Keys._
import sbt._

val slf4jVersion = "1.7.19"
val logBackVersion = "1.1.6"
val scalaLoggingVersion = "3.1.0"
val slickVersion = "3.1.1"
val seleniumVersion = "2.48.2"
val circeVersion = "0.4.0-RC1"
val ScalatraVersion = "2.4.0"

val slf4jApi = "org.slf4j" % "slf4j-api" % slf4jVersion
val logBackClassic = "ch.qos.logback" % "logback-classic" % logBackVersion
val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
val loggingStack = Seq(slf4jApi, logBackClassic, scalaLogging)

val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
val unitTestingStack = Seq(scalatest)

val seleniumJava = "org.seleniumhq.selenium" % "selenium-java" % seleniumVersion % "test"
val seleniumFirefox = "org.seleniumhq.selenium" % "selenium-firefox-driver" % seleniumVersion % "test"
val seleniumStack = Seq(seleniumJava, seleniumFirefox)


val scalatraStack = Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,

  "org.scalatra" %% "scalatra-swagger" % ScalatraVersion,
  "org.json4s"   %% "json4s-native" % "3.3.0.RC6",

  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

scalaVersion := "2.11.8"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val commonDependencies = unitTestingStack ++ loggingStack

val gdbscan = Seq(
  "org.scalanlp" %% "nak" % "1.3",
  "org.scalanlp" %% "breeze-natives" % "0.8" % "test, runtime"
)

lazy val commonSettings = Seq(
  organization := "com.afei",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-unchecked", "-deprecation"),
  version := "0.0.1-SNAPSHOT",
  libraryDependencies ++= commonDependencies
)


lazy val app = crossProject.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    unmanagedSourceDirectories in Compile += baseDirectory.value / "shared" / "main" / "scala",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "0.3.9",
      "com.lihaoyi" %%% "autowire" % "0.2.5"
    )
  ).jsSettings(
  name := "appJs-pro",
  libraryDependencies ++= Seq(
    "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
    "com.greencatsoft" %%% "scalajs-angular" % "0.6"
  ),

  skip in packageJSDependencies := false,

  jsDependencies ++= Seq(
    "org.webjars.bower" % "angular" % "1.5.1" / "angular.js",
    "org.webjars.bower" % "d3" % "3.5.16" / "d3.js",
    "org.webjars.bower" % "nvd3" % "1.8.2" / "nv.d3.js" dependsOn "d3.js",
    "org.webjars.bower" % "angular-nvd3" % "1.0.5" / "angular-nvd3.js" dependsOn "angular.js"
  ),

  //jsDependencies += RuntimeDOM

  // uTest settings
  libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
  testFrameworks += new TestFramework("utest.runner.Framework"),

  persistLauncher in Compile := true,
  persistLauncher in Test := false,

  artifactPath in(Compile, packageScalaJSLauncher) := baseDirectory.value / ".." / "jvm" / "src" / "main" / "webapp" / "js" / "launcher.js",
  artifactPath in(Compile, fastOptJS) := baseDirectory.value / ".." / "jvm" / "src" / "main" / "webapp" / "js" / "fastOpt.js",
  artifactPath in(Compile, fullOptJS) := baseDirectory.value / ".." / "jvm" / "src" / "main" / "webapp" / "js" / "fullOpt.js",
  artifactPath in(Compile, packageJSDependencies) := baseDirectory.value / ".." / "jvm" / "src" / "main" / "webapp" / "js" / "dependency.js"


).jvmSettings(ScalatraPlugin.scalatraSettings: _*)
  .jvmSettings(
    name := "appJvm-pro",
    libraryDependencies ++= scalatraStack ++ gdbscan,

    compile in Compile := {
      val compilationResult = (compile in Compile).value
      IO.touch(target.value / "compilationFinished")

      compilationResult
    }

  )

lazy val appJS = app.js.enablePlugins(ScalaJSPlugin)

lazy val appJVM = app.jvm.enablePlugins(JettyPlugin)

