package hyperpyramid

import hyperpyramid.WarehouseSettings.STORE_API_PASSWORD
import hyperpyramid.WarehouseSettings.STORE_API_USER
import hyperpyramid.WarehouseSettings.STORE_URL
import hyperpyramid.adapter.HttpDepartmentStore
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.domain.Warehouse
import hyperpyramid.endpoint.DispatchItems
import hyperpyramid.endpoint.ListItems
import hyperpyramid.port.Inventory
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.events.Events
import org.http4k.routing.routes
import java.time.Clock

fun WarehouseApi(
    env: Environment,
    clock: Clock,
    events: Events,
    http: HttpHandler,
    inventory: Inventory
): HttpHandler {
    val appEvents = AppEvents("warehouse", clock, events)
    val outgoingHttp = AppOutgoingHttp(appEvents, http)

    val warehouse = Warehouse(
        inventory,
        HttpDepartmentStore(
            Credentials(env[STORE_API_USER], env[STORE_API_PASSWORD]), env[STORE_URL],
            outgoingHttp
        )
    )

    return AppIncomingHttp(
        appEvents,
        routes(
            ListItems(warehouse),
            DispatchItems(warehouse)
        )
    )
}

