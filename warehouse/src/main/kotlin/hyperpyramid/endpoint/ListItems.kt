package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.InventoryItem
import hyperpyramid.port.WarehouseHub
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.bind

fun ListItems(hub: WarehouseHub) = "/v1/items" bind GET to {
    hub.items()
        .map { Response(OK).with(Body.auto<List<InventoryItem>>().toLens() of it) }
        .orThrow()
}