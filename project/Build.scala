import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "hs-test"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
      "org.webjars" % "webjars-play" % "2.1.0",
      "org.webjars" % "jquery" % "1.9.1",
      "org.mongodb" %% "casbah" % "2.6.2"
      //"net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0",
    //jdbc,
    //anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
