package hyperpyramid.setup

import hyperpyramid.TheInternet
import org.http4k.cloudnative.env.Environment

/**
 * Apply all setup functions to the cloud environment.
 */
fun TheInternet.setupCloudEnvironment(vararg fn: CloudSetup) =
    listOf(
        ::SetRegion,
        ::PopulateImageServer,
        ::SetupOAuthClient
    )
        .map { it(this) }
        .fold(Environment.defaults(*fn)) { acc, updateCloud -> updateCloud(acc) }
