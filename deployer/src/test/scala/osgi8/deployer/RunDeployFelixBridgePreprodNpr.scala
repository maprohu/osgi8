package osgi8.deployer

import akka.http.scaladsl.model._

/**
  * Created by pappmar on 05/07/2016.
  */
object RunDeployFelixBridgePreprodNpr {

  def main(args: Array[String]) {

    RunDeployFelixBridgeNpr.run(
      Uri(s"http://qwls56:7036/npr-filter-tais-npr")
    )
  }


}
