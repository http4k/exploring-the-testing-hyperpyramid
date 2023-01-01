package exploring

import exploring.AuthSettings.DEV_MODE
import exploring.app.AppEvents
import exploring.app.AppIncomingHttp
import exploring.app.AppOutgoingHttp
import exploring.endpoint.Login
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.time.Clock
import java.time.Clock.systemUTC

fun Auth(
    env: Environment = ENV,
    events: Events = ::println,
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient()
): RoutingHttpHandler {
    val appEvents = AppEvents("warehouse", clock, events)
    val outgoingHttp = AppOutgoingHttp(DEV_MODE(env), appEvents, http)

    return AppIncomingHttp(
        DEV_MODE(env),
        appEvents, routes(
            Login()
        )
    )
}

