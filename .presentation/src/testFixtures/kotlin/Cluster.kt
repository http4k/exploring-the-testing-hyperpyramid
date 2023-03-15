import hyperpyramid.ApiGateway
import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.ApiGatewaySettings.WEBSITE_URL
import hyperpyramid.Warehouse
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

class Cluster(env: Environment, clock: Clock, events: Events, theInternet: HttpHandler) : HttpHandler {
    private val networkAccess = NetworkAccess()

    private val apiGateway = ApiGateway(env, clock, networkAccess, events)
    private val warehouse = Warehouse(env, clock, events, networkAccess, Inventory.InMemory(events, clock))
    private val website = Website(env, clock, events, networkAccess)

    init {
        networkAccess.http = routes(
            reverseProxyRouting(
                API_GATEWAY_URL(env).authority to apiGateway,
                WEBSITE_URL(env).authority to website,
                WAREHOUSE_URL(env).authority to warehouse
            ),
            Router.orElse bind theInternet,
        )
    }

    override fun invoke(request: Request) = networkAccess(request)
}

fun main() {
    Cluster(ENV, Clock.systemUTC(), ::println) { req: Request -> Response(Status.OK) }
}
