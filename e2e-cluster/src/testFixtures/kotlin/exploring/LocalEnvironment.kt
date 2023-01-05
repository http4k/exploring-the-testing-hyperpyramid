package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.ImageSettings.IMAGE_BUCKET
import exploring.setup.PopulateImageServer
import exploring.setup.SetupOAuthConnection
import exploring.setup.invoke
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.Uri
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val theInternet = TheInternet(
        mapOf(
            "cognito" to Uri.of("http://localhost:11000"),
            "dept-store" to Uri.of("http://dept-store"),
            "email" to Uri.of("http://email"),
            "s3" to Uri.of("http://s3")
        )
    )

    theInternet.cognito.asServer(SunHttp(11000)).start()

    val baseEnv = Environment.defaults(
        AWS_REGION of EU_WEST_1,
        IMAGE_BUCKET of BucketName.of("image-cache"),
        API_GATEWAY_URL of Uri.of("http://localhost:10000"),
        OAUTH_URL of Uri.of("http://localhost:11000"),
    )


    val env = listOf(::SetupOAuthConnection, ::PopulateImageServer).map { it(theInternet) }(baseEnv)

    Cluster(env, theInternet).asServer(SunHttp(10000)).start()

    println("Browse to ${API_GATEWAY_URL(env)}")
}
