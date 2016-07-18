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
    run(
      "osgi6",
      "1.0.6",
      "osgi6-pom"
    )
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
      dir
    )
  }

  def run(
    dir: String,
    version: String,
    tagBase: String
  ) : Unit = {
    val projectDir = new File("..") / dir

    val request = new DefaultInvocationRequest
    request.setPomFile(projectDir / "pom.xml")
    request.setGoals( Seq( "clean", "scm:checkout" ) )
    val props = new java.util.Properties()
    props.putAll(
      Map(
        "scmVersion" -> s"$tagBase-$version",
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
