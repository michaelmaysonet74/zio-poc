val zioVersion = "1.0.14"

lazy val compileDependencies = Seq(
  "dev.zio" %% "zio" % zioVersion
) map (_ % Compile)

lazy val settings = Seq(
  name := "zio-poc",
  version := "1.0.0",
  scalaVersion := "2.13.8",
  libraryDependencies ++= compileDependencies
)

lazy val root = (project in file("."))
  .settings(settings)
