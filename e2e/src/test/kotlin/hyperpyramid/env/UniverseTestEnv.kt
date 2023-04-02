package hyperpyramid.env

import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.ApiGatewaySettings.IMAGES_URL
import hyperpyramid.ApiGatewaySettings.SHOP_URL
import hyperpyramid.ShopApiSettings.NOTIFICATION_EMAIL_SENDER
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.WarehouseSettings.STORE_API_PASSWORD
import hyperpyramid.WarehouseSettings.STORE_API_USER
import hyperpyramid.WarehouseSettings.STORE_URL
import hyperpyramid.dto.Email
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.core.Uri

val UniverseTestEnv = Environment.defaults(
    AWS_REGION of EU_WEST_1,
    AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
    AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),

    NOTIFICATION_EMAIL_SENDER of Email.of("orders@http4k.org"),
    STORE_API_USER of "user",
    STORE_API_PASSWORD of "password",

    API_GATEWAY_URL of Uri.of("http://api-gateway"),
    IMAGES_URL of Uri.of("http://images"),
    SHOP_URL of Uri.of("http://shop"),
    STORE_URL of Uri.of("http://dept-store"),
    WAREHOUSE_URL of Uri.of("http://warehouse"),
)
