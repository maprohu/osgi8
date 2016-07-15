package osgi8.deployer

/**
  * Created by pappmar on 08/07/2016.
  */
object Bundles extends Bundles
trait Bundles {

  private val osgi6Version = "1.0.6-SNAPSHOT"
  private val nprVersion = "1.0.4"
  private val ftxVersion = "1.0.0-SNAPSHOT"
  private val vdm2cdfVersion = "1.0.0-SNAPSHOT"


  val admin =
    Bundle(
      "osgi6",
      "osgi6-admin",
      osgi6Version
    )

  val multiApi =
    Bundle(
      "osgi6",
      "osgi6-multi-api",
      osgi6Version
    )

  val multiBundle =
    Bundle(
      "osgi6",
      "osgi6-multi-bundle",
      osgi6Version
    )

  val strictApi =
    Bundle(
      "osgi6",
      "osgi6-strict-api",
      osgi6Version
    )

  val logging =
    Bundle(
      "osgi6",
      "osgi6-logging",
      osgi6Version
    )

  val strictMulti =
    Bundle(
      "osgi6",
      "osgi6-strict-multi-bundle",
      osgi6Version
    )

  val jolokia =
    Bundle(
      "osgi6",
      "osgi6-jolokia",
      osgi6Version
    )

  val osgi6 = Seq(
    logging,
    multiApi,
    multiBundle,
    strictApi,
    strictMulti,
    jolokia,
    admin
  )


  val reactiveStreams =
      Bundle(
        "org.reactivestreams",
        "reactive-streams",
        "1.0.0",
        ""
      )
  val nprApi =
      Bundle(
        "emsa",
        "npr-filter-api",
        nprVersion
      )
  val nprCore =
      Bundle(
        "emsa",
        "npr-filter-core",
        nprVersion
      )
  val npr = Seq(
    reactiveStreams,
    nprApi,
    nprCore
  )

  val ftxOvr =
    Bundle(
      "emsa",
      "frontex-ovr",
      ftxVersion
    )

  val jms11 =
    Bundle(
      "osgi6",
      "osgi6-fragment-jms-1.1",
      osgi6Version
    )

  val vdm2cdf =
    Bundle(
      "emsa",
      "vdm2cdf-core",
      vdm2cdfVersion
    )

}
