import ApiGatewaySettings.API_GATEWAY_URL
import ApiGatewaySettings.SHOP_URL
import hyperpyramid.ApiGateway
import hyperpyramid.WebsiteSettings.WAREHOUSE_URL
import hyperpyramid.adapter.InMemory
import hyperpyramid.http.NetworkAccess
import hyperpyramid.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.events.Events
import org.http4k.routing.Router
import org.http4k.routing.bind
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

class EcommerceSystem(env: Environment, clock: Clock, events: Events, theInternet: HttpHandler) : HttpHandler {
    private val networkAccess = NetworkAccess()

    private val apiGateway = ApiGateway(env, clock, networkAccess, events)
    private val warehouse = WarehouseApi(env, clock, events, networkAccess, Inventory.InMemory(events, clock))
    private val shop = ShopApi(env, clock, events, networkAccess)

    init {
        networkAccess.http = routes(
            reverseProxyRouting(
                env[API_GATEWAY_URL].authority to apiGateway,
                env[SHOP_URL].authority to shop,
                env[WAREHOUSE_URL].authority to warehouse
            ),
            Router.orElse bind theInternet,
        )
    }

    override fun invoke(request: Request) = networkAccess(request)
}

fun main() {
    EcommerceSystem(ENV, Clock.systemUTC(), ::println) { req: Request -> Response(Status.OK) }
}