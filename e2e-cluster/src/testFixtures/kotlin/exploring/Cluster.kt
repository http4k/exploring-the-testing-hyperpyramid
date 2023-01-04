package exploring

import exploring.adapter.InMemory
import exploring.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting
import org.http4k.routing.routes
import java.time.Clock

fun Cluster(
    env: Environment,
    theInternet: RoutingHttpHandler,
    events: Events = ::println,
    clock: Clock = Clock.systemUTC()
): HttpHandler {
    val networkAccess = NetworkAccess()

    networkAccess.http = routes(
        theInternet,
        reverseProxyRouting(
            "api-gateway" to ApiGateway(env, events, clock, networkAccess),
            "images" to Images(env, events, clock, networkAccess),
            "warehouse" to Warehouse(env, events, clock, networkAccess, Inventory.InMemory(events, clock)),
            "website" to Website(env, events, clock, networkAccess)
        )
    )

    return networkAccess
}
