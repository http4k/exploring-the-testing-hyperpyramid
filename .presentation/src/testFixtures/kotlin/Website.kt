import hyperpyramid.WebsiteSettings.DEBUG
import hyperpyramid.WebsiteSettings.WAREHOUSE_URL
import hyperpyramid.adapter.Http
import hyperpyramid.adapter.SES
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.port.Notifications
import hyperpyramid.port.Warehouse
import hyperpyramid.port.WebsiteHub
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

fun Website(env: Environment, events: Events, clock: Clock, http: HttpHandler): RoutingHttpHandler {
    val appEvents = AppEvents("Website", clock, events)
    val outgoingHttp = AppOutgoingHttp(DEBUG(env), appEvents, http)

    val hub = WebsiteHub(appEvents,
        Warehouse.Http(WAREHOUSE_URL(env), outgoingHttp),
        Notifications.SES(env, outgoingHttp))

    return AppIncomingHttp(
        DEBUG(env),
        appEvents, routes(
            PlaceOrder(hub),
            ListAllItems(hub)
        )
    )
}

fun main() {
    Website(ENV,::println, Clock.systemUTC(), { req: Request -> Response(Status.OK) } )
}

fun ListAllItems(hub: WebsiteHub): RoutingHttpHandler = TODO()
fun PlaceOrder(hub: WebsiteHub): RoutingHttpHandler = TODO()
