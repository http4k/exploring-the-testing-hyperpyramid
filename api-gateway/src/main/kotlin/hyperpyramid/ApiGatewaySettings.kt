package hyperpyramid

import org.http4k.config.EnvironmentKey
import org.http4k.lens.of
import org.http4k.lens.uri

object ApiGatewaySettings : Settings() {
    val API_GATEWAY_URL by EnvironmentKey.uri().of().required()
    val IMAGES_URL by EnvironmentKey.uri().of().required()
    val SHOP_URL by EnvironmentKey.uri().of().required()
}
