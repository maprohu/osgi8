package osgi8.deployer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO

import scala.concurrent.Await
import scala.concurrent.duration.Duration


/**
  * Created by pappmar on 06/07/2016.
  */
object RunDeployTomatCommand {

  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()

    OsgiDeployer.getBundle(
      Bundle(
        "osgi6",
        "osgi6-command",
        "1.0.2-SNAPSHOT"
      )
    ) { bnd =>
      println(bnd.length())

      val fut =
        Http().singleRequest(
          HttpRequest(
            method = HttpMethods.PUT,
            uri = "http://localhost:9977/osgitest/deploy",
            entity = HttpEntity(
              ContentTypes.`application/octet-stream`,
              FileIO.fromPath(bnd.toPath)
            )
          )
        )

      println(Await.result(fut, Duration.Inf))
    }

  }

}
