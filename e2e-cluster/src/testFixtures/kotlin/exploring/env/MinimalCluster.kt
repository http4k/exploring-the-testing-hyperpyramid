package exploring.env

import exploring.Cluster
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.LocalhostServiceDiscovery
import exploring.TheInternet
import exploring.setup.setupCloudEnvironmentUsing
import exploring.start
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.s3.model.BucketName

/**
 * This starts up the minimum amount of servers: the ApiGateway and Cognito. Everything else
 * is in-process.
 */
fun main() {
    val services = LocalhostServiceDiscovery("api-gateway", "cognito")

    val theInternet = TheInternet(services).apply {
        cognito.start(services, "cognito")
    }

    val env = theInternet.setupCloudEnvironmentUsing(
        AWS_REGION of EU_WEST_1,
        IMAGE_BUCKET of BucketName.of("image-cache")
    )

    Cluster(env, services, theInternet).apply {
        apiGateway.start(services, "api-gateway")
    }
}
