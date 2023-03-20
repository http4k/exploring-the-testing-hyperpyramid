import hyperpyramid.WebsiteSettings.DEBUG
import hyperpyramid.WebsiteSettings.WAREHOUSE_URL
import hyperpyramid.adapter.Http
import hyperpyramid.adapter.SES
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.port.Notifications
import hyperpyramid.port.Warehouse
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.time.Clock

fun ShopApi(env: Environment, clock: Clock, events: Events, http: HttpHandler): RoutingHttpHandler {
    val appEvents = AppEvents("Shop", clock, events)
    val outgoingHttp = AppOutgoingHttp(DEBUG(env), appEvents, http)

    val shop = Shop(appEvents,
        Warehouse.Http(WAREHOUSE_URL(env), outgoingHttp),
        Notifications.SES(env, outgoingHttp))

    return AppIncomingHttp(
        DEBUG(env),
        appEvents, routes(
            PlaceOrder(shop),
            ListAllItems(shop)
        )
    )
}

fun main() {
    ShopApi(ENV, Clock.systemUTC(), ::println) { req: Request -> Response(Status.OK) }
}

fun ListAllItems(hub: Shop): RoutingHttpHandler = TODO()
fun PlaceOrder(hub: Shop): RoutingHttpHandler = TODO()
