package osgi8.deployer

/**
  * Created by pappmar on 08/07/2016.
  */
object Bundles extends Bundles
trait Bundles {

  private val osgi6Version = "1.0.3-SNAPSHOT"
  private val nprVersion = "1.0.1-SNAPSHOT"

  val osgi6 = Seq(
    Bundle(
      "osgi6",
      "osgi6-console",
      osgi6Version
    ),
    Bundle(
      "osgi6",
      "osgi6-logging",
      osgi6Version
    ),
    Bundle(
      "osgi6",
      "osgi6-multi-api",
      osgi6Version
    ),
    Bundle(
      "osgi6",
      "osgi6-strict-api",
      osgi6Version
    ),
    Bundle(
      "osgi6",
      "osgi6-strict-bundle",
      osgi6Version
    ),
    Bundle(
      "osgi6",
      "osgi6-deploy",
      osgi6Version
    ),
    Bundle(
      "osgi6",
      "osgi6-command",
      osgi6Version
    )
  )

  val npr = Seq(
      Bundle(
        "org.reactivestreams",
        "reactive-streams",
        "1.0.0",
        ""
      ),
      Bundle(
        "emsa",
        "npr-filter-api",
        nprVersion
      ),
      Bundle(
        "emsa",
        "npr-filter-core",
        nprVersion
      )
    )


}
