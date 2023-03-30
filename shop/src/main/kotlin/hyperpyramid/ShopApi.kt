package hyperpyramid

import hyperpyramid.ShopApiSettings.DEBUG
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.adapter.HttpWarehouse
import hyperpyramid.adapter.SESNotifications
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.endpoint.ListAllItems
import hyperpyramid.endpoint.PlaceOrder
import hyperpyramid.port.Shop
import hyperpyramid.util.Json
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import java.time.Clock
import java.time.Clock.systemUTC

fun ShopApi(
    env: Environment = ENV,
    events: Events = AutoMarshallingEvents(Json),
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient()
): RoutingHttpHandler {
    val appEvents = AppEvents("website", clock, events)
    val outgoingHttp = AppOutgoingHttp(env[DEBUG], appEvents, http)

    val templateRenderer = HandlebarsTemplates().CachingClasspath()
    val shop = Shop(appEvents, HttpWarehouse(env[WAREHOUSE_URL], outgoingHttp), SESNotifications(env, outgoingHttp))

    return AppIncomingHttp(
        env[DEBUG],
        appEvents, routes(
            PlaceOrder(shop, templateRenderer),
            ListAllItems(shop, templateRenderer)
        )
    )
}

