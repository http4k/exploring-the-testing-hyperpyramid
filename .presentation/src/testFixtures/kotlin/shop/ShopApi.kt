package shop

import hyperpyramid.ShopApiSettings.DEBUG
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.adapter.HttpWarehouse
import hyperpyramid.adapter.SESNotifications
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
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

fun ShopApi(env: Environment, clock: Clock, events: Events, http: HttpHandler): HttpHandler {
    val appEvents = AppEvents("Shop", clock, events)
    val outgoingHttp = AppOutgoingHttp(env[DEBUG], appEvents, http)

    val shop = Shop(appEvents,
        HttpWarehouse(env[WAREHOUSE_URL], outgoingHttp),
        SESNotifications(env, outgoingHttp))

    return AppIncomingHttp(
        env[DEBUG],
        appEvents, routes(PlaceOrder(shop), ListAllItems(shop))
    )
}

fun main() {
    ShopApi(ENV, Clock.systemUTC(), ::println) { req: Request -> Response(Status.OK) }
}

fun ListAllItems(shop: Shop): RoutingHttpHandler = TODO()
fun PlaceOrder(shop: Shop): RoutingHttpHandler = TODO()
