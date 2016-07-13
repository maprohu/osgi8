package osgi8.deployer.wupdata

import akka.http.scaladsl.model.Uri

/**
  * Created by martonpapp on 12/07/16.
  */
trait WupdataUrls {

  val wupdataLocalRoot = Uri("http://localhost:7002/wupdata")
  val wupdataLocal = wupdataLocalRoot.withPath(wupdataLocalRoot.path / "service")

}
