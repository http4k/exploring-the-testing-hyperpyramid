package exploring

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    env: Environment = ENV,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC(),
    theInternet: RoutingHttpHandler
): HttpHandler {
    val networkAccess = NetworkAccess()

    val auth = Auth(env, events, clock, networkAccess)

    val internalHttp = reverseProxyRouting(
        "reservations" to Reservations(env, events, clock, networkAccess),
        "warehouse" to Warehouse(env, events, clock, networkAccess),
        "auth" to auth
    )

    networkAccess.http = routes(internalHttp, theInternet)

    return ApiGateway(env, events, clock, networkAccess)
}
