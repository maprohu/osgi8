package osgi8.deployer

import akka.http.scaladsl.model.Uri

/**
  * Created by pappmar on 08/07/2016.
  */
trait Urls {

  val tomcatNpr = Uri(s"http://localhost:9977/npr-filter-tais-npr")
  val testNpr = Uri(s"http://twls55:7030/npr-filter-tais-npr")
  val testFtx = Uri(s"http://twls57:7037/ftx-core")
  val pp55Npr = Uri(s"http://qwls55:7036/npr-filter-tais-npr")
  val pp56Npr = Uri(s"http://qwls56:7036/npr-filter-tais-npr")

}

object Urls extends Urls
