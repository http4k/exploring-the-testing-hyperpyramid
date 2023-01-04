package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.OAUTH_CLIENT_ID
import exploring.ApiGatewaySettings.OAUTH_CLIENT_SECRET
import exploring.ApiGatewaySettings.OAUTH_URL
import exploring.ApiGatewaySettings.WEBSITE_URL
import exploring.WarehouseSettings.DATABASE_DRIVER
import exploring.WarehouseSettings.DATABASE_URL
import exploring.WarehouseSettings.STORE_API_PASSWORD
import exploring.WarehouseSettings.STORE_API_USER
import exploring.WarehouseSettings.STORE_URL
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import exploring.WebsiteSettings.WAREHOUSE_URL
import exploring.adapter.InMemory
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.ARN
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    theInternet: RoutingHttpHandler,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC()
): HttpHandler {
    val commonEnv = Environment.defaults(
        DEBUG of true,
        AWS_REGION of EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
    )
    val apiGatewayEnv = Environment.defaults(
        API_GATEWAY_URL of Uri.of("http://api-gateway"),
        WEBSITE_URL of Uri.of("http://website"),
        OAUTH_URL of Uri.of("http://cognito"),
        OAUTH_CLIENT_ID of "apiGatewayClient",
        OAUTH_CLIENT_SECRET of "apiGatewaySecret",
    )
    val websiteEnv = Environment.defaults(
        NOTIFICATION_TOPIC_ARN of ARN.parse("arn:aws:sns:us-east-2:123456789012:MyExampleTopic"),
        WAREHOUSE_URL of Uri.of("http://warehouse"),
    )
    val warehouseEnv = Environment.defaults(
        DATABASE_URL of "jdbc:h2:mem:warehouse;DB_CLOSE_DELAY=-1",
        DATABASE_DRIVER of "org.h2.Driver",
        STORE_URL of Uri.of("http://dept-store"),
        STORE_API_USER of "user",
        STORE_API_PASSWORD of "password"
    )

    val env = commonEnv overrides apiGatewayEnv overrides websiteEnv overrides warehouseEnv

    val networkAccess = NetworkAccess()

    val internalHttp = reverseProxyRouting(
        "api-gateway" to ApiGateway(env, events, clock, networkAccess),
        "warehouse" to Warehouse(env, events, clock, networkAccess, Inventory.InMemory(events, clock)),
        "website" to Website(env, events, clock, networkAccess)
    )

    networkAccess.http = routes(theInternet, internalHttp)

    return networkAccess
}
