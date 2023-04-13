package hyperpyramid

import hyperpyramid.dto.Email
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.of
import org.http4k.lens.uri
import org.http4k.lens.value

object ShopApiSettings : Settings() {
    val NOTIFICATION_EMAIL_SENDER by EnvironmentKey.value(Email).of().required()
    val WAREHOUSE_URL by EnvironmentKey.uri().of().required()
}
