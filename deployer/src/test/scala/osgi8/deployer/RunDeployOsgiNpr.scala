package osgi8.deployer

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{FileIO, Sink}
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by pappmar on 05/07/2016.
  */
object RunDeployOsgiNpr {

  def main(args: Array[String]) {

    val target =
      Uri(s"http://localhost:7002/npr-filter-tais-npr")
    run(target)
  }

  def run(target: Uri) = {
    implicit val actorSystem = ActorSystem()
    import actorSystem.dispatcher
    implicit val materializer = ActorMaterializer()

    val bundles = Seq(
      Bundle(
        "org.reactivestreams",
        "reactive-streams",
        "1.0.0"
      ),
      Bundle(
        "emsa",
        "npr-filter-api",
        "1.0.1-SNAPSHOT"
      ),
      Bundle(
        "emsa",
        "npr-filter-core",
        "1.0.1-SNAPSHOT"
      )
    )



    OsgiDeployer.getBundles(
      bundles.map({ bundle =>
        (
          bundle,
          (file:File) => {
            println(file.length())
            Await.result(
              for {
                _ <- upload(
                  file,
                  target.withPath(target.path / "deploy")
                )
              } yield (),
              Duration.Inf
            )
          }
        )
      }):_*
    )

  }

  def upload(
    what: File,
    target: Uri
  )(implicit
    actorSystem: ActorSystem,
    materializer: Materializer
  ) = {
    import actorSystem.dispatcher
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.PUT,
        uri = target,
        entity = HttpEntity(
          ContentTypes.`application/octet-stream`,
          FileIO.fromPath(what.toPath)
        )
      )
    ).flatMap(r => {
      println(r)
      require(r.status.isSuccess())
      r.entity.dataBytes.runWith(Sink.ignore)
    })
  }

  def command(
    target: Uri,
    cmd: String
  )(implicit
    actorSystem: ActorSystem,
    materializer: Materializer
  ) = {
    println(cmd)
    import actorSystem.dispatcher
    for {
      r <- Http().singleRequest(
        HttpRequest(
          method = HttpMethods.PUT,
          uri = target
            .withPath(target.path / "command")
            .withQuery(Query(
              "cmd" -> cmd
            ))
        )
      )
      str <- Unmarshal(r.entity).to[String]
    } yield {
      println(r)
      println(str)
      require(r.status.isSuccess())
    }
  }


}
