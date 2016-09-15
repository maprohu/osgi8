package osgi8.testing.wup

import akka.actor.ActorSystem
import akka.http.javadsl.server.CustomRejection
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.http.scaladsl.model.{HttpRequest => _, HttpResponse => _, _}
import akka.http.scaladsl.server.{PathMatcher, PathMatcher0, PathMatchers, Route}
import akka.stream.ActorMaterializer
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.HttpHeaders.Names
import io.netty.handler.codec.http._
import org.littleshoot.proxy.{HttpFilters, HttpFiltersAdapter, HttpFiltersSourceAdapter}
import osgi8.testing.TestingProxy

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.collection.immutable._

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
//    val redirectTo = Uri("http://pwls56:7036/wupdata/service/public")

    val redirectRoute : Route =
      path("imdate_bs" / "wup_data_dir" / Segments) { segs =>
        extractRequest { req =>
          onComplete(
            Http().singleRequest(
              req.copy(
                uri = redirectTo.withPath(
                  segs.foldLeft(redirectTo.path)(_ / _)
                ).withQuery(
                  req.uri.query()
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

      override def getMaximumResponseBufferSizeInBytes: Int = 10 * 1024 * 1024
      override def getMaximumRequestBufferSizeInBytes: Int = 10 * 1024 * 1024

      override def filterRequest(originalRequest: HttpRequest): HttpFilters = {
        new HttpFiltersAdapter(originalRequest, null) {
          override def clientToProxyRequest(httpObject: HttpObject): HttpResponse = {

            originalRequest match {
              case dhr : DefaultFullHttpRequest =>

                val method = HttpMethods.getForKey(dhr.getMethod().name().toUpperCase).get

                if (method != HttpMethods.CONNECT) {

                  val uri = Uri.parseHttpRequestTarget(dhr.getUri)
                  println(method)
                  println(uri)
                  import scala.collection.JavaConversions._
                  val request = akka.http.scaladsl.model.HttpRequest(
                    //                  uri = Uri(s"http://localhost${dhr.getUri}"),
                    method = method,
                    uri = uri,
                    headers = dhr.headers().names()
                      .filter({ headerName =>
                        headerName != akka.http.scaladsl.model.headers.`Content-Type`.name &&
                          headerName != akka.http.scaladsl.model.headers.`Content-Length`.name
                      })
                      .flatMap({ headerName =>
                        dhr.headers().getAll(headerName).map({ headerValue =>
                          HttpHeader.parse(headerName, headerValue).asInstanceOf[Ok].header
                        })
                      }).to[Seq],
                    entity = HttpEntity(
                      contentType =
                        Option(
                          dhr.headers().get(akka.http.scaladsl.model.headers.`Content-Type`.name)
                        ).map({ ct =>
                          ContentType.parse(ct).right.get
                        }).getOrElse(
                          ContentTypes.`application/octet-stream`
                        ),
                      bytes = {
                        val content = dhr.content()
                        val array = Array.ofDim[Byte](content.readableBytes())
                        content.readBytes(array)
                        array
                      }
                    )
                  )

                  val result = Await.result(
                    handler(request),
                    Duration.Inf
                  )

                  if (result.status.intValue() != NotProxiedCode) {
                    //                  println(originalRequest)

                    val strict = Await.result(result.entity.toStrict(10.minutes), Duration.Inf)
                    val bytes = strict.data.toArray
                    val dfhr = new DefaultFullHttpResponse(
                      dhr.getProtocolVersion,
                      new HttpResponseStatus(result.status.intValue(), result.status.defaultMessage()),
                      Unpooled.wrappedBuffer(bytes)
                    )
                    result.headers.foreach({ header =>
                      dfhr.headers().add(header.name(), header.value())
                    })
                    dfhr.headers().add(Names.CONTENT_LENGTH, bytes.length)

                    return dfhr
                  }

                }

              case _ =>
            }
//            println()

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
