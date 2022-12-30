package exploring

import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Clock
import java.time.Clock.systemUTC

/**
 * The API Gateway is the entry point to our platform. It provides a security layer and reverse
 * proxies traffic to the internal applications
 */
fun ApiGateway(
    env: Environment = ENV,
    events: Events = ::println,
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient()
): HttpHandler {
    val appEvents = AppEvents("api-gateway", clock, events)
    val outgoingHttp = OutgoingHttp(http, appEvents)

    fun ForwardTrafficToApp(app: String) = SetHostFrom(Uri.of("http://$app")).then(outgoingHttp)

    return IncomingHttp(
        appEvents, routes(
            "/v1/login" bind ForwardTrafficToApp("auth"),
            "/v1/reserve" bind ForwardTrafficToApp("reservations")
        )
    )
}
