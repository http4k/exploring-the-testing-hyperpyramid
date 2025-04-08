package hyperpyramid.setup

import hyperpyramid.TheInternet
import org.http4k.config.Environment

/**
 * Apply all setup functions to the cloud environment.
 */
fun TheInternet.createCloudResourcesAndEnv(vararg fn: CloudInfraSetup) =
    listOf(
        ::SetRegion,
        ::PopulateImageServer,
    )
        .map { it(this) }
        .fold(Environment.defaults(*fn)) { acc, updateCloud -> updateCloud(acc) }
