package exploring

import exploring.WebsiteSettings.DEV_MODE
import exploring.adapter.Http
import exploring.adapter.SNS
import exploring.app.AppEvents
import exploring.app.AppIncomingHttp
import exploring.app.AppOutgoingHttp
import exploring.endpoint.ListAllItems
import exploring.endpoint.PlaceOrder
import exploring.port.Notifications
import exploring.port.Warehouse
import exploring.port.WebsiteHub
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
    val outgoingHttp = AppOutgoingHttp(DEV_MODE(env), appEvents, http)

    val templateRenderer = HandlebarsTemplates().CachingClasspath()
    val hub = WebsiteHub(Warehouse.Http(outgoingHttp), Notifications.SNS(env, outgoingHttp))

    return AppIncomingHttp(
        DEV_MODE(env),
        appEvents, routes(
            PlaceOrder(hub, templateRenderer),
            ListAllItems(hub, templateRenderer)
        )
    )
}

