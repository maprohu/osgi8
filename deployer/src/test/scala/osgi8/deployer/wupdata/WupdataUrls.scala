package osgi8.deployer.wupdata

import akka.http.scaladsl.model.Uri

/**
  * Created by martonpapp on 12/07/16.
  */
trait WupdataUrls {

  val wupdataTwls55Root = Uri("http://twls55:7030/wupdata")
  val wupdataTwls55 = wupdataTwls55Root.withPath(wupdataTwls55Root.path / "service")

  val wupdataLocalRoot = Uri("http://localhost:7002/wupdata")
  val wupdataLocal = wupdataLocalRoot.withPath(wupdataLocalRoot.path / "service")


}
