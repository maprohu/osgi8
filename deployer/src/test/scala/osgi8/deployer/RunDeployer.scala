package osgi8.deployer

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.scaladsl.FileIO
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

/**
  * Created by pappmar on 08/07/2016.
  */
object RunDeployer extends Urls with Bundles {

  def main(args: Array[String]) {
    run(
      Urls.testNpr
    )
  }

  trait Runner {
    implicit val actorSystem : ActorSystem
    implicit val materializer : Materializer
    implicit val executionContext : ExecutionContext
  }

  case class RunnerImpl(
    actorSystem: ActorSystem,
    materializer: Materializer,
    executionContext: ExecutionContext
  ) extends Runner

  def perform[T](fn: Runner => Future[T]) : Future[T] = {
    implicit val actorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import actorSystem.dispatcher

    val runner = RunnerImpl(actorSystem, materializer, actorSystem.dispatcher)

    val f = for {
      v <- fn(runner)
      _ <- Http().shutdownAllConnectionPools()
      _ <- actorSystem.terminate()
    } yield (
      v
    )

    f.onComplete(println)

    f
  }

  def run(target: Uri) = {
    perform { r =>
      import r._

      for {
        _ <- uninstallBundles(target, 16L to 42L)
        _ <- installBundles(target, Bundles.osgi6 ++ Bundles.npr)
      } yield ()
    }

  }

  def uninstallBundles(target: Uri, ids: Seq[Long])(implicit
    executionContext: ExecutionContext,
    actorSystem: ActorSystem,
    materializer: Materializer
  ) = {
    runSeq(
      ids
    )(
      undeploy(target)
    )
  }

  def installBundles(target: Uri, ids: Seq[Bundle])(implicit
    executionContext: ExecutionContext,
    actorSystem: ActorSystem,
    materializer: Materializer
  ) = {
    OsgiDeployer.getBundlesAll(
      ids
    ) { files =>
      Await.result(
        runSeq(
          files
        )(
          deploy(target)
        ),
        Duration.Inf
      )
    }

    Future.successful(())
  }

  def foldSeq[T, V](zero: V, items: Seq[T])(fn: (T, V) => Future[V])(
    implicit executionContext: ExecutionContext
  ) : Future[V] = {
    items match {
      case head +: tail =>
        fn(head, zero).flatMap({ v =>
          foldSeq(v, tail)(fn)
        })
      case _ =>
        Future.successful(zero)
    }
  }

  def runSeq[T](items: Seq[T])(fn: T => Future[Any])(
    implicit executionContext: ExecutionContext
  ) : Future[Unit] = {
    foldSeq((), items)((item, _) => fn(item).map(_ => ()))
  }


  def undeploy(target: Uri)(id: Long)(implicit
    actorSystem: ActorSystem,
    materializer: Materializer
  ) : Future[HttpResponse] = {

    request(
      HttpRequest(
        uri = target
          .withPath(
            target.path / "_admin" / "undeploy"
          )
          .withQuery(
            Query(
              "id" -> id.toString
            )
          )
      )
    )

  }

  def deploy(target: Uri)(file: File)(implicit
    actorSystem: ActorSystem,
    materializer: Materializer
  ) : Future[HttpResponse] = {

    request(
      HttpRequest(
        method = HttpMethods.PUT,
        uri = target
          .withPath(
            target.path / "_admin" / "deploy"
          ),
        entity =
          HttpEntity(
            ContentTypes.`application/octet-stream`,
            FileIO.fromPath(file.toPath)
          )
      )
    )

  }

  def refresh(target: Uri)(implicit
    actorSystem: ActorSystem,
    materializer: Materializer
  ) : Future[HttpResponse] = {

    request(
      HttpRequest(
        method = HttpMethods.PUT,
        uri = target
          .withPath(
            target.path / "_admin" / "refresh"
          )
      )
    )

  }

  def request(req: HttpRequest)(implicit
    actorSystem: ActorSystem,
    materializer: Materializer
  ) : Future[HttpResponse] = {
    import actorSystem.dispatcher

    println(req)
    Http().singleRequest(
      req
    ).flatMap({ res =>
      res.entity.toStrict(1.minute).map{ se =>
        val sres = res.withEntity(se)
        println(sres)
        sres
      }
    })

  }
}
