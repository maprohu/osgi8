package osgi8.deployer

/**
  * Created by pappmar on 08/07/2016.
  */
object RunAdHoc {

  import RunDeployer._

  def main(args: Array[String]) {

    perform { r => import r._
//      uninstallBundles(tomcatNpr, Seq(1L))
//      uninstallBundles(tomcatNpr, 9L to 10 )
//      installBundles(tomcatNpr, Seq(nprCore))
//      installBundles(tomcatNpr, npr)
//      installBundles(tomcatNpr, Seq( nprApi, nprCore ) )

//      uninstallBundles(tomcatNpr, 9l to 10 )
//      uninstallBundles(tomcatNpr, 11L to 20L )
//      installBundles(tomcatNpr, osgi6 ++ npr)

//      uninstallBundles(testNpr, 11L to 12 )
//      installBundles(testNpr, Seq( nprApi, nprCore ) )

//      uninstallBundles(wupdataLocal, 1L to 65 )

//      installBundles(wupdataLocal, Seq( logging, wupdataCore ) )

      installBundles(wupdataLocal, Seq( h2gis, wupdataGeoserver ) )



      // fixing
//      uninstallBundles(wupdataLocal, Seq( 77L ) )
//      installBundles(wupdataLocal, Seq( wupdataCore ) )

//      installBundles(wupdataLocal, Seq( wupdataContextBridge ) )
//      installBundles(wupdataLocal, Seq( wupdataMultiBridge ) )
//      installBundles(wupdataLocal, Seq( jms11 ) )

//      uninstallBundles(wupdataLocal, (1L to 65) ++ Seq(69L) )

//      uninstallBundles(wupdataLocal, 1L to 65 )
//      installBundles(pp56Npr, Seq( nprApi, nprCore ) )

//      installBundles(testNpr, Seq( nprCore ) )
//      uninstallBundles(testNpr, Seq( 10L ) )
//      installBundles(pp56Npr, Seq( nprCore ) )

      // !!!!!!!!! patch prerod !!!!!!!!!!!!!!
//      installBundles(prod55Npr, Seq( nprCore ) )
//      uninstallBundles(prod55Npr, Seq( 10L ) )

//      uninstallBundles(testVdm, Seq( 9L ) )
//      installBundles(testVdm, Seq( vdm2cdf ) )


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
