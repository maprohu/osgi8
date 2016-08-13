package osgi8.testing

import org.littleshoot.proxy.extras.SelfSignedMitmManager
import org.littleshoot.proxy.impl.DefaultHttpProxyServer

/**
  * Created by martonpapp on 13/08/16.
  */
object TestingProxy {

  def run = {
    val mitm = new SelfSignedMitmManager

    DefaultHttpProxyServer.bootstrap()
      .withPort(7783)
      .withManInTheMiddle(mitm)
      .start()
  }


}
