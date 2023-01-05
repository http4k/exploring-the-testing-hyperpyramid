package exploring.env

import exploring.Cluster
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.LocalhostServiceDiscovery
import exploring.ServiceDiscovery
import exploring.TheInternet
import exploring.setup.setupCloudEnvironmentUsing
import exploring.start
import org.http4k.client.JavaHttpClient
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.routing.reverseProxy


/**
 * This starts up the all services on their own port.
 */
fun main() {
    val services = LocalhostServiceDiscovery(
        "api-gateway", "images", "warehouse", "website", // cluster services
        "cognito", "dept-store", "email", "s3" // external services
    )

    val theInternet = TheInternet(services).apply {
        cognito.start(services, "cognito")
        departmentStore.start(services, "dept-store")
        ses.start(services, "email")
        s3.start(services, "s3")
    }

    val env = theInternet.setupCloudEnvironmentUsing(
        AWS_REGION of EU_WEST_1,
        IMAGE_BUCKET of BucketName.of("image-cache"),
    )

    val realHttp = RealHttpFor(services)

    Cluster(env, services, realHttp).apply {
        apiGateway.start(services, "api-gateway")
        images.start(services, "images")
        warehouse.start(services, "warehouse")
        website.start(services, "website")
    }
}

fun RealHttpFor(services: ServiceDiscovery) = reverseProxy(
    *listOf(
        "api-gateway", "images", "warehouse", "website", // cluster services
        "cognito", "dept-store", "email", "s3" // external services
    )
        .map { it to SetBaseUriFrom(services(it)).then(JavaHttpClient()) }
        .toTypedArray()
)

