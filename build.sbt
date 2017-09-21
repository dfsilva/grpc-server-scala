import com.typesafe.sbt.SbtMultiJvm.multiJvmSettings
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm
import com.trueaccord.scalapb.compiler.Version.{grpcJavaVersion, scalapbVersion, protobufVersion}

val akkaVersion = "2.5.4"

val unusedWarnings = (
  "-Ywarn-unused" ::
    "-Ywarn-unused-import" ::
    Nil
  )

val commonSettings: Seq[Def.Setting[_]] = Seq[Def.Setting[_]](
  organization := "br.com.diegosilva.grpc-server",
  scalaVersion := "2.12.3",
  scalacOptions in Compile ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
  javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
  javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
    "io.grpc" % "grpc-netty" % grpcJavaVersion,
    "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    "io.kamon" % "sigar-loader" % "1.6.6-rev002"),
  parallelExecution in Test := false,
  licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))
)

lazy val root = project.in(file("."))
  .settings(multiJvmSettings: _*)
  .settings(
    commonSettings,
    mainClass in (Compile, run) := Some("GameApp"),
    fork in run := true,
  )
  .configs (MultiJvm)
  .dependsOn(services)
  .aggregate(services, core)

lazy val core = project.in(file("core"))
  .settings(
  commonSettings
)

lazy val services = project
  .in(file("services"))
  .settings(
    commonSettings,
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  ).dependsOn(core)

        