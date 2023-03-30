package hyperpyramid

import hyperpyramid.ApiGatewaySettings.DEBUG
import hyperpyramid.ApiGatewaySettings.IMAGES_URL
import hyperpyramid.ApiGatewaySettings.WEBSITE_URL
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.util.Json
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.then
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.routing.Router.Companion.orElse
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
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient(),
    events: Events = AutoMarshallingEvents(Json)
): HttpHandler {
    val appEvents = AppEvents("api-gateway", clock, events)
    val outgoingHttp = AppOutgoingHttp(env[DEBUG], appEvents, http)

    val oAuthProvider = ApiGatewayOAuthProvider(env, outgoingHttp)

    return AppIncomingHttp(
        env[DEBUG],
        appEvents, routes(
            "/oauth/callback" bind GET to oAuthProvider.callback,
            "/img/{.+}" bind SetHostFrom(env[IMAGES_URL]).then(outgoingHttp),
            orElse bind
                oAuthProvider.authFilter
                    .then(SetHostFrom(env[WEBSITE_URL]))
                    .then(outgoingHttp)
        )
    )
}
