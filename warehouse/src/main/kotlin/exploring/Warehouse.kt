package exploring

import exploring.WarehouseSettings.DEV_MODE
import exploring.WarehouseSettings.STORE_API_PASSWORD
import exploring.WarehouseSettings.STORE_API_USER
import exploring.WarehouseSettings.STORE_URL
import exploring.adapter.Database
import exploring.adapter.Http
import exploring.app.AppEvents
import exploring.app.AppIncomingHttp
import exploring.app.AppOutgoingHttp
import exploring.endpoint.DispatchItems
import exploring.endpoint.ListItems
import exploring.port.DepartmentStore
import exploring.port.Inventory
import exploring.port.WarehouseHub
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.Credentials
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

    val hub = WarehouseHub(
        inventory, DepartmentStore.Http(
            Credentials(STORE_API_USER(env), STORE_API_PASSWORD(env)),
            STORE_URL(env), outgoingHttp
        )
    )

    return AppIncomingHttp(
        DEV_MODE(env),
        appEvents, routes(
            ListItems(hub),
            DispatchItems(hub)
        )
    )
}

