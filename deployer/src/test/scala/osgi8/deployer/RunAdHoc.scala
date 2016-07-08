package osgi8.deployer

/**
  * Created by pappmar on 08/07/2016.
  */
object RunAdHoc {

  import RunDeployer._

  def main(args: Array[String]) {

    perform { r => import r._
//      uninstallBundles(testNpr, 61L to 70 )
//      installBundles(testNpr, osgi6 ++ npr)
      installBundles(pp56Npr, osgi6 ++ npr)

//      uninstallBundles(pp56Npr, (16L to 19) ++ (33L to 42) )
    }


  }

}
