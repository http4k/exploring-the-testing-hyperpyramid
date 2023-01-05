package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.app.Debug
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.server.SunHttp
import org.http4k.server.asServer

private const val DEBUG_FLAG = true

fun main() {
    val theInternet = TheInternet(
        mapOf(
            "cognito" to Uri.of("http://localhost:11000"),
            "dept-store" to Uri.of("http://dept-store"),
            "email" to Uri.of("http://email"),
            "s3" to Uri.of("http://s3")
        )
    )

    Debug(DEBUG_FLAG).then(theInternet.cognito).asServer(SunHttp(11000)).start()

    val env = Environment.defaults(
        DEBUG of DEBUG_FLAG,
        API_GATEWAY_URL of Uri.of("http://localhost:10000"),
        OAUTH_URL of Uri.of("http://localhost:11000"),
    )

    Cluster(env, theInternet).asServer(SunHttp(10000)).start()

    println("Browse to ${API_GATEWAY_URL(env)}")
}
