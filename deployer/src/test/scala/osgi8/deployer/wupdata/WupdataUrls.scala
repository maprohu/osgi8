package osgi8.deployer.wupdata

import akka.http.scaladsl.model.Uri

/**
  * Created by martonpapp on 12/07/16.
  */
trait WupdataUrls {

  val wupdataLocal = Uri("http://localhost:7002/wupdata")

}
