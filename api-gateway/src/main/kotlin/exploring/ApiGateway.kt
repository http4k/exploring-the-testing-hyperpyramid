package exploring

import exploring.ApiGatewaySettings.DEBUG
import exploring.ApiGatewaySettings.WEBSITE_URL
import exploring.app.AppEvents
import exploring.app.AppIncomingHttp
import exploring.app.AppOutgoingHttp
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.routing.asRouter
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
    val outgoingHttp = AppOutgoingHttp(DEBUG(env), appEvents, http)

    val oAuthProvider = ApiGatewayOAuthProvider(env, outgoingHttp)

    return AppIncomingHttp(
        DEBUG(env),
        appEvents, routes(
            "/oauth/callback" bind GET to oAuthProvider.callback,
            { _: Request -> true }.asRouter() bind
                oAuthProvider.authFilter
                    .then(SetHostFrom(WEBSITE_URL(env)))
                    .then(outgoingHttp),
        )
    )
}
