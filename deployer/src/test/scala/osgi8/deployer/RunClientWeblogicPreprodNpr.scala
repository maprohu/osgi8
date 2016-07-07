package osgi8.deployer

/**
  * Created by martonpapp on 30/06/16.
  */
object RunClientWeblogicPreprodNpr {

  def main(args: Array[String]) {
    FelixClientLoop.run(
      app = "npr-filter-tais-npr",
      host = "qwls56",
      port = 7036
    )
  }

}
