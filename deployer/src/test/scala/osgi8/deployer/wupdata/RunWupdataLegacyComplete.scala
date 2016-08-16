package osgi8.deployer.wupdata

/**
  * Created by pappmar on 16/08/2016.
  */
object RunWupdataLegacyComplete {

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    for {
      uploadedFileNames <- RunWupdataLegacy.runUpload
    } {

    }

  }

}
