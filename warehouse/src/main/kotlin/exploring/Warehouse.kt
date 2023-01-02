package exploring

import exploring.WarehouseSettings.DEV_MODE
import exploring.adapter.Database
import exploring.adapter.SQS
import exploring.app.AppEvents
import exploring.app.AppIncomingHttp
import exploring.app.AppOutgoingHttp
import exploring.endpoint.DispatchItems
import exploring.endpoint.ListItems
import exploring.port.Dispatcher
import exploring.port.Inventory
import exploring.port.WarehouseHub
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.time.Clock
import java.time.Clock.systemUTC

fun Warehouse(
    env: Environment = ENV,
    events: Events = ::println,
    clock: Clock = systemUTC(),
    http: HttpHandler = JavaHttpClient(),
    inventory: Inventory = Inventory.Database(env)
): RoutingHttpHandler {

    val appEvents = AppEvents("warehouse", clock, events)
    val outgoingHttp = AppOutgoingHttp(DEV_MODE(env), appEvents, http)

    val hub = WarehouseHub(inventory, Dispatcher.SQS(env, outgoingHttp))
    return AppIncomingHttp(
        DEV_MODE(env),
        appEvents, routes(
            ListItems(hub),
            DispatchItems(hub)
        )
    )
}

