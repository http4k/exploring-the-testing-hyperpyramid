package hyperpyramid

import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.ApiGatewaySettings.IMAGES_URL
import hyperpyramid.ApiGatewaySettings.SHOP_URL
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.adapter.InMemoryInventory
import hyperpyramid.http.NetworkAccess
import org.http4k.cloudnative.env.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.events.Events
import org.http4k.routing.Router.Companion.orElse
import org.http4k.routing.bind
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

class EcommerceSystem(
    env: Environment,
    clock: Clock,
    events: Events,
    theInternet: HttpHandler
) : HttpHandler {

    private val networkAccess = NetworkAccess()

    val apiGateway = ApiGateway(env, clock, events, networkAccess)
    val images = ImagesApi(env, clock, events, networkAccess)
    val warehouse = WarehouseApi(env, clock, events, networkAccess, InMemoryInventory(events, clock))
    val shop = ShopApi(env, clock, events, networkAccess)

    init {
        networkAccess.http = routes(
            reverseProxyRouting(
                env[API_GATEWAY_URL].authority to apiGateway,
                env[IMAGES_URL].authority to images,
                env[WAREHOUSE_URL].authority to warehouse,
                env[SHOP_URL].authority to shop
            ),
            orElse bind theInternet
        )
    }

    override fun invoke(p1: Request) = networkAccess(p1)
}
