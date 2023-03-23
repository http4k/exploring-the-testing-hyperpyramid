import hyperpyramid.WarehouseSettings.DEBUG
import hyperpyramid.WarehouseSettings.STORE_API_PASSWORD
import hyperpyramid.WarehouseSettings.STORE_API_USER
import hyperpyramid.WarehouseSettings.STORE_URL
import hyperpyramid.adapter.Database
import hyperpyramid.adapter.Http
import hyperpyramid.app.AppEvents
import hyperpyramid.app.AppIncomingHttp
import hyperpyramid.app.AppOutgoingHttp
import hyperpyramid.endpoint.DispatchItems
import hyperpyramid.endpoint.ListItems
import hyperpyramid.port.DepartmentStore
import hyperpyramid.port.Inventory
import hyperpyramid.port.WarehouseHub
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
    inventory: Inventory = Inventory.Database(env)
): RoutingHttpHandler {
    val appEvents = AppEvents("warehouse", clock, events)
    val outgoingHttp = AppOutgoingHttp(env[DEBUG], appEvents, http)

    val hub = WarehouseHub(
        inventory,
        DepartmentStore.Http(
            Credentials(env[STORE_API_USER], env[STORE_API_PASSWORD]), env[STORE_URL],
            outgoingHttp
        )
    )

    return AppIncomingHttp(
        env[DEBUG],
        appEvents, routes(
            ListItems(hub),
            DispatchItems(hub)
        )
    )
}

