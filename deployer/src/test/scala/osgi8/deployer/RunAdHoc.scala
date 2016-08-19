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
//            uninstallBundles(wupdataLocal, (1L to 65) ++ Seq(69L) )

//      installBundles(wupdataLocal, Seq( h2gis, wupdataBsh ) )
//      installBundles(wupdataLocal, Seq(  wupdataBsh ) )


      // !!!  Installing wupdata after legacy
//      installBundles(wupdataTwls55, Seq( logging, wupdataCore ) )
//      installBundles(wupdataTwls55, Seq( wupdataMultiBridge ) )
//      uninstallBundles(wupdataTwls55, (1L to 65) ++ Seq(380L) )  // karaf + legacy
//      installBundles(wupdataLocal, Seq( h2gis, wupdataBsh ) )
//      uninstallBundles(wupdataLocal, Seq( 75L, 76 ) )
      // run data upload
      // http://localhost:7002/wupdata/service/public/api/bsh/rendered/wupext/bsh/shared/BshRenderedApi/daysList


//      uninstallBundles(wupdataLocal, Seq( 84L, 90 ) )
      uninstallBundles(wupdataLocal, Seq( 87L ) )
//      uninstallBundles(wupdataTwls55, Seq( 71L ) )
//      uninstallBundles(wupdataTwls55, Seq( 384L ) )
//      installBundles(wupdataLocal, Seq( adminExec ) )
//      installBundles(wupdataLocal, Seq( wupdataCore ) )
//      installBundles(wupdataLocal, Seq( wupdataBsh ) )

      // fixing
//      installBundles(wupdataLocal, Seq( wupdataGeoserver ) )
//      uninstallBundles(wupdataLocal, Seq( 97L ) )
//      uninstallBundles(wupdataLocal, Seq( 94L, 94 ) )
//      uninstallBundles(wupdataLocal, Seq( 71L, 79L, 80L ) )
//      installBundles(wupdataLocal, Seq( wupdataCore ) )

//      installBundles(wupdataLocal, Seq( wupdataContextBridge ) )
//      installBundles(wupdataLocal, Seq( wupdataMultiBridge ) )
//      installBundles(wupdataLocal, Seq( jms11 ) )


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
