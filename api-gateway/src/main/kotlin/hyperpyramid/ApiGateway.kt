package hyperpyramid

import hyperpyramid.ApiGatewaySettings.IMAGES_URL
import hyperpyramid.ApiGatewaySettings.SHOP_URL
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import org.http4k.config.Environment
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.routing.bind
import org.http4k.routing.orElse
import org.http4k.routing.routes
import java.time.Clock

/**
 * The API Gateway is the entry point to our platform. It provides a security layer and reverse
 * proxies traffic to the internal applications
 */
fun ApiGateway(
    env: Environment,
    clock: Clock,
    events: Events,
    http: HttpHandler
): HttpHandler {
    val appEvents = AppEvents("api-gateway", clock, events)
    val outgoingHttp = AppOutgoingHttp(appEvents, http)

    return AppIncomingHttp(
        appEvents,
        routes(
            "/img/{.+}" bind SetHostFrom(env[IMAGES_URL]).then(outgoingHttp),
            orElse bind SetHostFrom(env[SHOP_URL]).then(outgoingHttp)
        )
    )
}
