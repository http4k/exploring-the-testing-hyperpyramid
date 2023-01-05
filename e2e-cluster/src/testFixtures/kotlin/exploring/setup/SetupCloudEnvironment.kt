package exploring.setup

import exploring.TheInternet
import org.http4k.cloudnative.env.Environment

/**
 * Apply all setup functions to the cloud environment.
 */
fun TheInternet.setupCloudEnvironmentUsing(vararg fn: CloudSetup) =
    listOf(
        ::PopulateImageServer,
        ::SetupOAuthClient
    )
        .map { it(this) }
        .fold(Environment.defaults(*fn)) { acc, updateCloud -> updateCloud(acc) }
