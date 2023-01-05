package hyperpyramid

import hyperpyramid.WebsiteSettings.DEBUG
import hyperpyramid.WebsiteSettings.WAREHOUSE_URL
import hyperpyramid.adapter.Http
import hyperpyramid.adapter.SES
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.endpoint.ListAllItems
import hyperpyramid.endpoint.PlaceOrder
import hyperpyramid.port.Notifications
import hyperpyramid.port.Warehouse
import hyperpyramid.port.WebsiteHub
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import java.time.Clock
import java.time.Clock.systemUTC

fun Website(
    env: Environment = ENV,
    events: Events = ::println,
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient()
): RoutingHttpHandler {
    val appEvents = AppEvents("website", clock, events)
    val outgoingHttp = AppOutgoingHttp(DEBUG(env), appEvents, http)

    val templateRenderer = HandlebarsTemplates().CachingClasspath()
    val hub = WebsiteHub(appEvents, Warehouse.Http(WAREHOUSE_URL(env), outgoingHttp), Notifications.SES(env, outgoingHttp))

    return AppIncomingHttp(
        DEBUG(env),
        appEvents, routes(
            PlaceOrder(hub, templateRenderer),
            ListAllItems(hub, templateRenderer)
        )
    )
}

