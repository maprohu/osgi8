package osgi8.deployer.wupdata

import osgi8.deployer.Bundle

/**
  * Created by pappmar on 13/07/2016.
  */
trait WupdataBundles {

  val wupdataVersion = "1.0.0-SNAPSHOT"

  val wupdataCore =
    Bundle(
      "emsa",
      "wupdata-core",
      wupdataVersion
    )

  val wupdataLegacy =
    Bundle(
      "emsa",
      "wupdata-legacy",
      wupdataVersion
    )

  val wupdataContextBridge =
    Bundle(
      "emsa",
      "wupdata-context-bridge",
      wupdataVersion
    )

  val wupdataMultiBridge =
    Bundle(
      "emsa",
      "wupdata-multi-bridge",
      wupdataVersion
    )

}
