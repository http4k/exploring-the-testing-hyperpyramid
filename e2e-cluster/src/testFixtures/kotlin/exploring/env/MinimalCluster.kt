package exploring.env

import exploring.Cluster
import exploring.TheInternet
import exploring.http.LocalhostServiceDiscovery
import exploring.http.start
import exploring.setup.setupCloudEnvironment

/**
 * This starts up the minimum amount of servers: the ApiGateway and Cognito. Everything else
 * is in-process.
 */
fun main() {
    val services = LocalhostServiceDiscovery(10000, "api-gateway", "cognito")

    val theInternet = TheInternet(services).apply {
        cognito.start(services, "cognito")
    }

    val env = theInternet.setupCloudEnvironment()

    Cluster(env, services, theInternet).apply {
        apiGateway.start(services, "api-gateway")
    }
}
