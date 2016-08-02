package osgi8.deployer.frontex

import java.io.File

import osgi8.deployer.{Bundle, OsgiDeployer, RunDeployer}
import sbt.io.IO
import sbt.io.Path._

import scala.concurrent.Future
import scala.xml.XML

/**
  * Created by martonpapp on 28/07/16.
  */
object RunReleaseFtxCore {

  val scalaExtVersion = "1.0.3"
  val osgi6Version = "1.0.8-SNAPSHOT"
  val frontexCoreVersion = "1.0.1-SNAPSHOT"

  def main(args: Array[String]) {

    val targetDir = new File("../frontex-osgi/target/release")
    IO.delete(targetDir)
    targetDir.mkdirs()

    val extDirName = "ext"
    val libsDirName = "libs"
    val appDirName = "app"

    val rootPom =
      <project xmlns="http://maven.apache.org/POM/4.0.0"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>

        <groupId>emsa</groupId>
        <artifactId>frontex-build</artifactId>
        <version>1.0.1</version>
        <packaging>pom</packaging>

        <modules>
          <module>{extDirName}</module>
          <module>{libsDirName}</module>
          <module>{appDirName}</module>
        </modules>
      </project>

    XML.save(
      (targetDir / "pom.xml").absolutePath,
      rootPom
    )


    import RunDeployer._


    val osgi6Bundle =
      Bundle(
        "osgi6",
        "osgi6-pom",
        osgi6Version,
        "project",
        "zip"
      )

    val ftxBundle =
      Bundle(
        "emsa",
        "frontex-pom",
        frontexCoreVersion,
        "project",
        "zip"
      )

    val scalaExtBundle =
      Bundle(
        "maprohu",
        "scala-ext",
        scalaExtVersion,
        "project",
        "zip"
      )

    def item(bundle: Bundle, dir: String) : (Bundle, File => Unit) = {
      bundle -> { f =>
        IO.unzip(f, targetDir)
        (targetDir / s"${bundle.artifact}-${bundle.version}") renameTo (targetDir / dir)
      }
    }


    perform { r => import r._

      OsgiDeployer.getBundles(
        item(scalaExtBundle, extDirName),
        item(osgi6Bundle, libsDirName),
        item(ftxBundle, appDirName)
      )

      Future.successful()
    }




  }

}
