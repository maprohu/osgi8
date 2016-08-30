package osgi8.deployer

import java.io.File

import org.apache.maven.shared.invoker.{DefaultInvocationRequest, DefaultInvoker}
import sbt.io.Path._

import scala.collection.JavaConversions._

/**
  * Created by pappmar on 12/07/2016.
  */
object RunCheckout {

  def main(args: Array[String]): Unit = {
//    run(
//      "npr-filter-osgi",
//      "1.0.2",
//      "npr-filter-pom"
//    )
//    runTag(
//      "../dev/wsij/frontex-wms",
//      "0.20.0"
//    )
//    run(
//      "../dev/wsij/frontex-cor-parser",
//      "1.0"
//    )
//    run(
//      "../dev/wsij/frontex-cdf-parser",
//      "1.0.3"
//    )
//    run(
//      "../dev/wsij/frontex-csn-parser",
//      "1.0"
//    )
//    run(
//      "../dev/wsij/common-libs",
//      "0.0.15",
//      "common-libs"
//    )
//    run(
//      "../dev/wsij/frontex-parent",
//      "0.0.1",
//      "frontex-parent"
//    )
//    run(
//      "osgi6-parent",
//      "1.0.3"
//    )
//    run(
//      "osgi6",
//      "1.0.10",
//      "osgi6-pom"
//    )
//    run(
//      "osgi6-h2gis",
//      "1.0.10"
//    )
    run(
      "osgi6-logging",
      "1.0.10"
    )
//    run(
//      "osgi6-libs",
//      "1.0.12"
//    )
//    run(
//      "osgi6-multi-api",
//      "1.0.13"
//    )
//    run(
//      "scala-ext",
//      "1.0.2"
//    )
  }

  def run(
    dir: String,
    version: String
  ) : Unit = {
    run(
      dir,
      version,
      new File(dir).name
    )
  }

  def run(
    dir: String,
    version: String,
    tagBase: String
  ) : Unit = {
    runTag(
      dir,
      s"${tagBase}-${version}"
    )

  }

  def runTag(
    dir: String,
    tag: String
  ) : Unit = {
    val projectDir = new File("..") / dir

    val request = new DefaultInvocationRequest
    request.setPomFile(projectDir / "pom.xml")
    request.setGoals( Seq( "clean", "scm:checkout" ) )
    val props = new java.util.Properties()
    props.putAll(
      Map(
        "scmVersion" -> tag,
        "scmVersionType" -> "tag"
      )
    )
    request.setProperties(props)
    val invoker = new DefaultInvoker
    val result = invoker.execute(request)

    if (result.getExitCode == 0) {
      request.setPomFile(projectDir / "target" / "checkout" / "pom.xml")
      request.setGoals( Seq( "install" ) )
      invoker.execute(request)


    }
  }

}
