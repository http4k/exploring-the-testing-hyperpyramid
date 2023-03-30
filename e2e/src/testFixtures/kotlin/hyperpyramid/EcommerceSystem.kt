package hyperpyramid

import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.ApiGatewaySettings.IMAGES_URL
import hyperpyramid.ApiGatewaySettings.OAUTH_URL
import hyperpyramid.ApiGatewaySettings.SHOP_URL
import hyperpyramid.ShopApiSettings.NOTIFICATION_EMAIL_SENDER
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.WarehouseSettings.STORE_API_PASSWORD
import hyperpyramid.WarehouseSettings.STORE_API_USER
import hyperpyramid.WarehouseSettings.STORE_URL
import hyperpyramid.adapter.InMemoryInventory
import hyperpyramid.dto.Email
import hyperpyramid.http.NetworkAccess
import hyperpyramid.http.ServiceDiscovery
import hyperpyramid.util.Json
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
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Events
import org.http4k.routing.asRouter
import org.http4k.routing.bind
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

class EcommerceSystem(
    customEnv: Environment,
    services: ServiceDiscovery,
    theInternet: HttpHandler,
    events: Events = AutoMarshallingEvents(Json),
    clock: Clock = Clock.systemUTC()
) : HttpHandler {

    private val env = customEnv overrides defaults(
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
        SHOP_URL of services("shop")
    )

    private val networkAccess = NetworkAccess()

    val apiGateway = ApiGateway(env, clock, networkAccess, events)
    val images = ImagesApi(env, events, clock, networkAccess)
    val warehouse = WarehouseApi(env, clock, events, networkAccess, InMemoryInventory(events, clock))
    val shop = ShopApi(env, events, clock, networkAccess)

    init {
        networkAccess.http = routes(
            reverseProxyRouting(
                env[API_GATEWAY_URL].authority to apiGateway,
                env[IMAGES_URL].authority to images,
                env[WAREHOUSE_URL].authority to warehouse,
                env[SHOP_URL].authority to shop
            ),
            { _: Request -> true }.asRouter() bind theInternet,
        )
    }

    override fun invoke(p1: Request) = networkAccess(p1)
}
