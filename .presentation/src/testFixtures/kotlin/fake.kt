import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun FakeWarehouse(): HttpHandler {
    val stockLevels = mutableMapOf(ItemId.of("1") to 5)

    val listItems = { _: Request ->
        Response(OK)
            .with(
                Body.auto<List<InventoryItem>>().toLens() of
                    stockLevels.map { (item, stock) ->
                        InventoryItem(item, "bar", stock)
                    }
            )
    }
    val dispatch = { req: Request ->
        val order = Body.auto<Shipment>().toLens()(req)
        stockLevels[order.id] = stockLevels[order.id]!! - order.amount
        Response(ACCEPTED)
    }
    return routes("/v1/items" bind GET to listItems, "/v1/dispatch" bind POST to dispatch)
}

fun main() {
    FakeWarehouse().asServer(SunHttp()).start()
}

data class Shipment(
    val customer: String,
    val id: ItemId,
    val amount: Int
) {
    operator fun minus(number: Int) = amount - number
}
