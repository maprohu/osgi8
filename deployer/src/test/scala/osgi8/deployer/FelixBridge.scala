package osgi8.deployer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, StreamConverters}
import emsa.felix.deploy.{Bundle, OsgiDeployer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by pappmar on 05/07/2016.
  */
object FelixBridge {

  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()

    OsgiDeployer.getBundle(
      Bundle(
        "osgi6",
        "osgi6-api-bundle",
        "1.0.2-SNAPSHOT"
      )
    ) { dep =>

      val fut =
        Http().singleRequest(
          HttpRequest(
            method = HttpMethods.PUT,
            uri = "http://localhost:7002/npr-filter-tais-npr/repo/osgi-api.jar",
            entity = HttpEntity(
              ContentTypes.`application/octet-stream`,
              FileIO.fromPath(dep.toPath)
            )
          )
        )

      println(Await.result(fut, Duration.Inf))

      println("start file:/wl_domains/imdate/imdate-ext/data/npr-filter-tais-npr/repo/osgi-api.jar")

    }
//    OsgiDeployer.getBundle(
//      Bundle(
//        "emsa6",
//        "felix-osgi6-bridge",
//        "1.0.1-SNAPSHOT"
//      )
//    ) { dep =>
//      println(dep.length())
//    }

  }

}
