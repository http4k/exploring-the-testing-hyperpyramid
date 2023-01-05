package exploring

import exploring.ApiGatewaySettings.API_GATEWAY_URL
import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.IMAGES_URL
import exploring.ApiGatewaySettings.OAUTH_URL
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
import org.http4k.events.Events
import org.http4k.routing.asRouter
import org.http4k.routing.bind
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

class Cluster(
    customEnv: Environment,
    services: ServiceDiscovery,
    theInternet: HttpHandler,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC()
) : HttpHandler {

    private val env = customEnv overrides defaults(
        DEBUG of false,
        AWS_REGION of EU_WEST_1,
        AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
        AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
        NOTIFICATION_EMAIL_SENDER of Email.of("orders@http4k.org"),
        STORE_API_USER of "user",
        STORE_API_PASSWORD of "password",

        API_GATEWAY_URL of services("api-gateway"),
        IMAGES_URL of services("images"),
        OAUTH_URL of services("cognito"),
        STORE_URL of services("dept-store"),
        WAREHOUSE_URL of services("warehouse"),
        WEBSITE_URL of services("website")
    )

    private val networkAccess = NetworkAccess()

    val apiGateway = ApiGateway(env, events, clock, networkAccess)
    val images = Images(env, events, clock, networkAccess)
    val warehouse = Warehouse(env, events, clock, networkAccess, Inventory.InMemory(events, clock))
    val website = Website(env, events, clock, networkAccess)

    init {
        networkAccess.http = routes(
            reverseProxyRouting(
                API_GATEWAY_URL(env).authority to apiGateway,
                IMAGES_URL(env).authority to images,
                WAREHOUSE_URL(env).authority to warehouse,
                WEBSITE_URL(env).authority to website
            ),
            { _: Request -> true }.asRouter() bind theInternet,
        )
    }

    override fun invoke(p1: Request) = networkAccess(p1)
}
