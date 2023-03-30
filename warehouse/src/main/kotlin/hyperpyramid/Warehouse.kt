package hyperpyramid

import hyperpyramid.WarehouseSettings.DEBUG
import hyperpyramid.WarehouseSettings.STORE_API_PASSWORD
import hyperpyramid.WarehouseSettings.STORE_API_USER
import hyperpyramid.WarehouseSettings.STORE_URL
import hyperpyramid.adapter.DatabaseInventory
import hyperpyramid.adapter.HttpDepartmentStore
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.domain.Warehouse
import hyperpyramid.endpoint.DispatchItems
import hyperpyramid.endpoint.ListItems
import hyperpyramid.port.Inventory
import hyperpyramid.util.Json
import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.ENV
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Events
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.time.Clock
import java.time.Clock.systemUTC

fun WarehouseApi(
    env: Environment = ENV,
    clock: Clock = systemUTC(),
    events: Events = AutoMarshallingEvents(Json),
    http: HttpHandler = JavaHttpClient(),
    inventory: Inventory = DatabaseInventory(env)
): RoutingHttpHandler {
    val appEvents = AppEvents("warehouse", clock, events)
    val outgoingHttp = AppOutgoingHttp(env[DEBUG], appEvents, http)

    val warehouse = Warehouse(
        inventory,
        HttpDepartmentStore(
            Credentials(env[STORE_API_USER], env[STORE_API_PASSWORD]), env[STORE_URL],
            outgoingHttp
        )
    )

    return AppIncomingHttp(
        env[DEBUG],
        appEvents, routes(
            ListItems(warehouse),
            DispatchItems(warehouse)
        )
    )
}

