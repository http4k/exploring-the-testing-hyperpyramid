package hyperpyramid

import hyperpyramid.WarehouseSettings.STORE_API_PASSWORD
import hyperpyramid.WarehouseSettings.STORE_API_USER
import hyperpyramid.WarehouseSettings.STORE_URL
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.core.Uri

val WarehouseTestEnv = defaults(
    STORE_URL of Uri.of("http://store"),
    STORE_API_USER of "User",
    STORE_API_PASSWORD of "Password"
)
