package hyperpyramid

import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.adapter.HttpWarehouse
import hyperpyramid.adapter.SESNotifications
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.endpoint.ListAllItems
import hyperpyramid.endpoint.PlaceOrder
import hyperpyramid.port.Shop
import org.http4k.cloudnative.env.Environment
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.routes
import java.time.Clock

fun ShopApi(
    env: Environment,
    clock: Clock,
    events: Events,
    http: HttpHandler
): HttpHandler {
    val appEvents = AppEvents("shop", clock, events)
    val outgoingHttp = AppOutgoingHttp(appEvents, http)

    val shop = Shop(
        appEvents,
        HttpWarehouse(env[WAREHOUSE_URL], outgoingHttp),
        SESNotifications(env, outgoingHttp)
    )

    return AppIncomingHttp(
        appEvents,
        routes(PlaceOrder(shop), ListAllItems(shop))
    )
}
