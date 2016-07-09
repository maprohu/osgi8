package osgi8.deployer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by pappmar on 08/07/2016.
  */
object RunStress {

  import RunDeployer._

  def main(args: Array[String]) {
    import scala.concurrent.ExecutionContext.Implicits.global

    def deploy(count: Int): Future[_] = {
      val fut = for {
        _ <- perform { r => import r._
          uninstallBundles(tomcatNpr, count*10+1L to count*10+10 )
        }
        _ <- perform { r => import r._
          refresh(tomcatNpr)
        }
        _ <- perform { r => import r._
          installBundles(tomcatNpr, osgi6 ++ npr)
        }
//        _ <- perform { r => import r._
//          refresh(tomcatNpr)
//        }

      } yield {
        Thread.sleep(1000)
        ()
      }

      fut.flatMap{ _ =>
        deploy(count+1)
      }

    }

    Await.result(deploy(5), Duration.Inf)



  }

}
