package exploring

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.of
import org.http4k.lens.uri

object WarehouseSettings : Settings() {
    val DATABASE_URL by EnvironmentKey.of().required()
    val DATABASE_DRIVER by EnvironmentKey.of().required()
    val STORE_URL by EnvironmentKey.uri().of().required()
    val STORE_API_USER by EnvironmentKey.of().required()
    val STORE_API_PASSWORD by EnvironmentKey.of().required()
}
