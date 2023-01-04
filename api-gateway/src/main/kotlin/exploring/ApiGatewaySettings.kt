package exploring

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.of
import org.http4k.lens.uri

object ApiGatewaySettings : Settings() {
    val API_GATEWAY_URL by EnvironmentKey.uri().of().required()
    val WEBSITE_URL by EnvironmentKey.uri().of().required()
    val OAUTH_URL by EnvironmentKey.uri().of().required()
    val OAUTH_CLIENT_ID by EnvironmentKey.of().required()
    val OAUTH_CLIENT_SECRET by EnvironmentKey.of().required()
}
