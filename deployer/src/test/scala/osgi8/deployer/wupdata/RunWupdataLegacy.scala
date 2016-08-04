package osgi8.deployer.wupdata

import osgi8.deployer.{Bundle, Bundles, OsgiDeployer}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Created by martonpapp on 12/07/16.
  */
object RunWupdataLegacy extends Bundles /*with WupdataBundles*/ with WupdataUrls {


  import osgi8.deployer.RunDeployer._

  val target = wupdataLocalRoot


  def main(args: Array[String]) {

    perform { r => import r._

      OsgiDeployer.getBundlesAll(
        Seq(
          multiApi,
          wupdataContextBridge,
          admin,
          wupdataLegacy
        )
      ) { files =>
        Await.result(
          runSeq(
            files
          ) { f =>
            for {
              _ <- putFile(
                f,
                target.withPath(
                  target.path / "service" / "m2" / f.getName
                )
              )
            } yield {

            }
          },
          Duration.Inf
        )
      }

      Future.successful()
    }

  }

  /*
  file:/wl_domains/imdate/imdate-ext/data/wupdata-store/system/jar0
  file:/wl_domains/imdate/imdate-ext/data/wupdata-store/system/jar1
  file:/wl_domains/imdate/imdate-ext/data/wupdata-store/system/jar2
  file:/wl_domains/imdate/imdate-ext/data/wupdata-store/system/jar3

  http://localhost:7002/wupdata/hawtio/jolokia/exec/osgi.core%3Atype%3Dframework%2Cversion%3D1.7%2Cframework%3Dorg.apache.felix.framework%2Cuuid%3Dc1be870a-17c7-4f34-87a8-5baa1f6a56f9/installBundle(java.lang.String)/file%3A!%2Fwl_domains!%2Fimdate!%2Fimdate-ext!%2Fdata!%2Fwupdata-store!%2Fsystem!%2Fjar0?maxDepth=6&maxCollectionSize=500&ignoreErrors=true&canonicalNaming=false

{"timestamp":1468358751,"status":200,"request":{"operation":"installBundle(java.lang.String)","mbean":"osgi.core:type=framework,version=1.7,framework=org.apache.felix.framework,uuid=c1be870a-17c7-4f34-87a8-5baa1f6a56f9","arguments":["file:\/wl_domains\/imdate\/imdate-ext\/data\/wupdata-store\/system\/jar0"],"type":"exec"},"value":66}


   */

}
