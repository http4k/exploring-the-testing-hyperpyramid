package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.IMAGES_URL
import exploring.ApiGatewaySettings.WEBSITE_URL
import exploring.WarehouseSettings.STORE_API_PASSWORD
import exploring.WarehouseSettings.STORE_API_USER
import exploring.WarehouseSettings.STORE_URL
import exploring.WebsiteSettings.NOTIFICATION_EMAIL_SENDER
import exploring.WebsiteSettings.WAREHOUSE_URL
import exploring.adapter.InMemory
import exploring.dto.Email
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.defaults
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.events.Events
import org.http4k.routing.asRouter
import org.http4k.routing.bind
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    customEnv: Environment,
    theInternet: HttpHandler,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC()
): HttpHandler {
    val env = customEnv overrides defaults(
        AWS_REGION of EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
        IMAGES_URL of Uri.of("http://images"),
        NOTIFICATION_EMAIL_SENDER of Email.of("orders@http4k.org"),
        STORE_URL of Uri.of("http://dept-store"),
        STORE_API_USER of "user",
        STORE_API_PASSWORD of "password",
        WAREHOUSE_URL of Uri.of("http://warehouse"),
        WEBSITE_URL of Uri.of("http://website")
    )

    val networkAccess = NetworkAccess()

    val apiGateway = ApiGateway(env, events, clock, networkAccess)
    networkAccess.http = routes(
        reverseProxyRouting(
            API_GATEWAY_URL(env).authority to apiGateway,
            IMAGES_URL(env).authority to Images(env, events, clock, networkAccess),
            WAREHOUSE_URL(env).authority to Warehouse(env, events, clock, networkAccess, Inventory.InMemory(events, clock)),
            WEBSITE_URL(env).authority to Website(env, events, clock, networkAccess)
        ),
        { _: Request -> true }.asRouter() bind theInternet,
    )

    return networkAccess
}
