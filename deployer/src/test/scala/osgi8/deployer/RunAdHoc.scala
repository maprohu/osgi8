package osgi8.deployer

/**
  * Created by pappmar on 08/07/2016.
  */
object RunAdHoc {

  import RunDeployer._

  def main(args: Array[String]) {

    perform { r => import r._
//      uninstallBundles(tomcatNpr, Seq(1L))
//      uninstallBundles(tomcatNpr, 8L to 8 )
//      installBundles(tomcatNpr, Seq(nprCore))
//      installBundles(tomcatNpr, npr)

//      uninstallBundles(tomcatNpr, 1L to 8 )
//      uninstallBundles(tomcatNpr, 11L to 20L )
//      installBundles(tomcatNpr, osgi6 ++ npr)

//      uninstallBundles(testNpr, 105L to 106 )
//      installBundles(testNpr, Seq( nprApi, nprCore ) )

//      uninstallBundles(pp56Npr, 77L to 78 )
//      installBundles(pp56Npr, Seq( nprApi, nprCore ) )
//      installBundles(pp56Npr, osgi6 ++ npr)


//      installBundles(pp56Npr, Seq(
//        logging,
//        strictApi,
//        strictMulti,
//        jolokia
//      ) ++ npr )
//      uninstallBundles(testNpr, Seq(99L))
//      uninstallBundles(pp56Npr, Seq(75L))
//      installBundles(testNpr, Seq( multiApi, multiBundle, admin ) )
//      installBundles(pp56Npr, Seq( multiApi, multiBundle, admin ) )
//      installBundles(testNpr, Seq( admin ) )
//      installBundles(pp56Npr, Seq( jolokia ) )

//      uninstallBundles(testFtx, Seq(9L))
//      installBundles(testFtx, Seq( ftxOvr ) )
    }


  }

}
