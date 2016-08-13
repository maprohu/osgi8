package osgi8.testing.wup

import akka.actor.ActorSystem
import akka.http.javadsl.server.CustomRejection
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.{PathMatcher, PathMatcher0, PathMatchers, Route}
import akka.stream.ActorMaterializer
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.HttpHeaders.Names
import io.netty.handler.codec.http._
import org.littleshoot.proxy.{HttpFilters, HttpFiltersAdapter, HttpFiltersSourceAdapter}
import osgi8.testing.TestingProxy

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by martonpapp on 13/08/16.
  */
object RunProxyLocalTomcatProd {

  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import actorSystem.dispatcher

//    val wupdataUri = Uri("/imdate_bs/wup_data_dir")

    import akka.http.scaladsl.server.Directives._

    val NotProxiedCode = 778

    val redirectTo = Uri("http://localhost:9978/wupdev/data")

    val redirectRoute : Route =
      path("imdate_bs" / "wup_data_dir" / Segments) { segs =>
        extractRequest { req =>
          onComplete(
            Http().singleRequest(
              req.copy(
                uri = redirectTo.copy(
                  path = segs.foldLeft(redirectTo.path)(_ / _)
                )
              )
            )
          ) { r => complete(r) }
        }
      } ~ {
        complete(
          akka.http.scaladsl.model.HttpResponse(
            status = StatusCodes.custom(NotProxiedCode, "not proxied", "not proxied", false, false)
          )
        )
      }



    val handler = Route.asyncHandler(redirectRoute)



    val filterSource = new HttpFiltersSourceAdapter() {
      override def filterRequest(originalRequest: HttpRequest): HttpFilters = {
        new HttpFiltersAdapter(originalRequest, null) {
          override def clientToProxyRequest(httpObject: HttpObject): HttpResponse = {

            originalRequest match {
              case dhr : DefaultHttpRequest =>

                val request = akka.http.scaladsl.model.HttpRequest(
                  uri = Uri(s"http://localhost${dhr.getUri}")
                )

                val result = Await.result(
                  handler(request),
                  Duration.Inf
                )

                if (result.status.intValue() != NotProxiedCode) {
                  println(originalRequest)

                  val strict = Await.result(result.entity.toStrict(10.minutes), Duration.Inf)
                  val bytes = strict.data.toArray
                  val dfhr = new DefaultFullHttpResponse(
                    dhr.getProtocolVersion,
                    new HttpResponseStatus(result.status.intValue(), result.status.defaultMessage()),
                    Unpooled.wrappedBuffer(bytes)
                  )
                  dfhr.headers().add(Names.CONTENT_LENGTH, bytes.length)

                  return dfhr
                }



              case _ =>
            }
            println()

            null
          }
        }
      }
    }

    TestingProxy.createProxy
      .withFiltersSource(filterSource)
      .start()

  }

}