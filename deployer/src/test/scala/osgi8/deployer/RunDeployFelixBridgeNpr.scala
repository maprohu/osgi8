package osgi8.deployer

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.{FileIO, Sink, StreamConverters}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Created by pappmar on 05/07/2016.
  */
object RunDeployFelixBridgeNpr {

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
        "osgi6",
        "osgi6-api",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "osgi6",
        "osgi6-logging",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "osgi6",
        "osgi6-multi-api",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "osgi6",
        "osgi6-strict-api",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "osgi6",
        "osgi6-strict-bundle",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "osgi6",
        "osgi6-deploy",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "osgi6",
        "osgi6-command",
        "1.0.2-SNAPSHOT"
      ),
      Bundle(
        "emsa6",
        "felix-osgi6-bridge",
        "1.0.1-SNAPSHOT"
      )
    )



    OsgiDeployer.getBundles(
      bundles.map({ bundle =>
        (
          bundle,
          (file:File) => {
            Await.result(
              for {
                _ <- upload(
                  file,
                  target.withPath(
                    target.path / "repo" / s"${bundle.artifact}.jar"
                  )
                )
                _ <- {
                  Thread.sleep(1000)
                  Future.successful(())
                }
                _ <- command(
                  target,
                  s"start file:/wl_domains/imdate/imdate-ext/data/npr-filter-tais-npr/repo/${bundle.artifact}.jar"
                )
              } yield (),
              Duration.Inf
            )

            Thread.sleep(2000)
          }
        )
      }):_*
    )

    Thread.sleep(3000)

    command(
      target,
      s"uninstall ${((1 to 15) ++ (20 to 32)).mkString(" ")}"
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
