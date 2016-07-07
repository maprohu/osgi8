package osgi8.deployer

import akka.http.scaladsl.model._

/**
  * Created by pappmar on 05/07/2016.
  */
object RunDeployOsgiPreprodNpr {

  def main(args: Array[String]) {

    val target =
      Uri(s"http://qwls56:7036/npr-filter-tais-npr")

    RunDeployOsgiNpr.run(target)
  }


}
