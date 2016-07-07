package osgi8.deployer

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.FileIO
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by pappmar on 05/07/2016.
  */
object RunDeployOsgiTestNpr {

  def main(args: Array[String]) {

    val target =
      Uri(s"http://twls55:7030/npr-filter-tais-npr")

    RunDeployOsgiNpr.run(target)
  }


}
