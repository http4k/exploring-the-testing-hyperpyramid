package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.port.Shop
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.bind

fun ListAllItems(shop: Shop) = "/list" bind GET to {
    shop.items()
        .map { it.map(InventoryItem::id) }
        .map { Response(OK).with(Body.auto<List<ItemId>>().toLens() of it) }
        .orThrow()
}
