package exploring.env

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.Cluster
import exploring.TheInternet
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val theInternet = TheInternet()

    val env = Environment.defaults(
        DEBUG of true,
        API_GATEWAY_URL of Uri.of("http://localhost:10000"),
        OAUTH_URL of Uri.of("http://localhost:11000"),
    )

    Cluster(env, theInternet).asServer(SunHttp(10000)).start()
    theInternet.cognito.asServer(SunHttp(11000)).start()

    println("Browse to ${API_GATEWAY_URL(env)}")
}
