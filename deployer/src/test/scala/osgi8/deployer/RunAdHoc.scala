package osgi8.deployer

/**
  * Created by pappmar on 08/07/2016.
  */
object RunAdHoc {

  import RunDeployer._

  def main(args: Array[String]) {

    perform { r => import r._
      uninstallBundles(testNpr, 81L to 90 )
//      installBundles(testNpr, osgi6 ++ npr)

//      uninstallBundles(pp56Npr, 53L to 62)
//      installBundles(pp56Npr, osgi6 ++ npr)

    }


  }

}
