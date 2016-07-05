package emsa.felix.client

import osgi8.deployer.FelixClientLoop

/**
  * Created by martonpapp on 30/06/16.
  */
object RunClientWeblogicLocalNpr {

  def main(args: Array[String]) {
    FelixClientLoop.run(
      app = "npr-filter-tais-npr",
      port = 7002
    )
  }

}
