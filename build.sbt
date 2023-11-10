Global / onChangedBuildSource := ReloadOnSourceChanges

// https://typelevel.org/sbt-typelevel/faq.html#what-is-a-base-version-anyway
ThisBuild / tlBaseVersion := "0.1" // your current series x.y

ThisBuild / organization     := "io.github.mf42-dzh"
ThisBuild / organizationName := "Fawwaz Abdullah"
ThisBuild / startYear        := Some(2023)
ThisBuild / licenses         := Seq("BSD-3-Clause" -> url("https://opensource.org/licenses/BSD-3-Clause"))
ThisBuild / developers       := List(
  // your GitHub handle and name
  tlGitHubDev("mf42-dzh", "Fawwaz Abdullah")
)

// publish to s01.oss.sonatype.org (set to true to publish to oss.sonatype.org instead)
ThisBuild / tlSonatypeUseLegacyHost := false

val Scala212 = "2.12.18"
val Scala213 = "2.13.12"
val Scala3   = "3.3.1"

ThisBuild / crossScalaVersions := Seq(Scala212, Scala213, Scala3)
ThisBuild / scalaVersion       := Scala213 // the default Scala

// Java version for CI and support
ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("8"),
  JavaSpec.temurin("11"),
  JavaSpec.temurin("17")
)

// Shared dependencies for all frontends:
lazy val commonSettings = Seq(
  headerLicenseStyle   := HeaderLicenseStyle.SpdxSyntax,
  headerEmptyLine      := false,
  resolvers           ++= Opts.resolver.sonatypeOssSnapshots,
  libraryDependencies ++= Seq(
    "com.github.j-mie6" %%% "parsley"       % "4.4-a0b4baa-SNAPSHOT",
    "com.github.j-mie6" %%% "parsley-debug" % "4.4-a0b4baa-SNAPSHOT",
    "org.scalactic"     %%% "scalactic"     % "3.2.17" % Test,
    "org.scalatest"     %%% "scalatest"     % "3.2.17" % Test
  )
)

lazy val root = tlCrossRootProject.aggregate(con_ui, json_info, sfx_ui, http_server)

lazy val con_ui = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .withoutSuffixFor(JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("con-ui"))
  .settings(
    commonSettings,
    name := "parsley-debug-console"
  )

lazy val json_info = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .withoutSuffixFor(JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("json-info"))
  .settings(
    commonSettings,
    name                := "parsley-debug-json",
    libraryDependencies += "com.lihaoyi" %%% "ujson" % "3.0.0"
  )

lazy val sfx_ui = crossProject(JVMPlatform)
  .withoutSuffixFor(JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("sfx-ui"))
  .settings(
    commonSettings,
    name                := "parsley-debug-sfx",
    libraryDependencies += "org.scalafx" %%% "scalafx" % "19.0.0-R30"
  )

// Here's hoping the stable version of Http4S works fine!
val http4sVersion = "0.23.23"

lazy val http_server = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .withoutSuffixFor(JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("http-server"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-dsl"          % http4sVersion
    )
  )

Test / parallelExecution := false
