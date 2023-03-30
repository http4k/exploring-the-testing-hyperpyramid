package hyperpyramid.env

import hyperpyramid.EcommerceSystem
import hyperpyramid.TheInternet
import hyperpyramid.http.LocalhostServiceDiscovery
import hyperpyramid.http.ProxyCallToLiveServerFor
import hyperpyramid.http.start
import hyperpyramid.setup.setupCloudEnvironment


/**
 * This starts up the all services on their own port.
 */
fun main() {
    val services = LocalhostServiceDiscovery(
        10000,
        "api-gateway", "images", "warehouse", "website", // cluster services
        "cognito", "dept-store", "email", "s3" // external services
    )

    val theInternet = TheInternet(services).apply {
        cognito.start(services, "cognito")
        departmentStore.start(services, "dept-store")
        ses.start(services, "email")
        s3.start(services, "s3")
    }

    val env = theInternet.setupCloudEnvironment()

    val http = ProxyCallToLiveServerFor(services)

    EcommerceSystem(env, services, http).apply {
        apiGateway.start(services, "api-gateway")
        images.start(services, "images")
        warehouse.start(services, "warehouse")
        website.start(services, "website")
    }
}