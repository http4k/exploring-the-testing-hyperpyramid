package exploring

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.of
import org.http4k.lens.uri

object WarehouseSettings : Settings() {
    val DATABASE_URL by EnvironmentKey.of().required()
    val DATABASE_DRIVER by EnvironmentKey.of().required()
    val DISPATCH_QUEUE by EnvironmentKey.uri().of().required()
}
