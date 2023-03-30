package hyperpyramid.adapter

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.resultFrom
import hyperpyramid.dto.Email
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.ItemPickup
import hyperpyramid.dto.OrderId
import hyperpyramid.port.Warehouse
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters.SetHostFrom

fun HttpWarehouse(warehouseUri: Uri, rawHttp: HttpHandler) = object : Warehouse {
    private val http = SetHostFrom(warehouseUri).then(rawHttp)

    override fun items(): Result4k<List<InventoryItem>, Exception> {
        val resp = http(Request(GET, "/v1/items"))
        return when {
            resp.status.successful -> Success(Body.auto<List<InventoryItem>>().toLens()(resp))
            else -> Failure(Exception(resp.status.description))
        }
    }

    override fun dispatch(user: Email, id: ItemId): Result4k<OrderId, Exception> {
        val resp = http(
            Request(POST, "/v1/dispatch")
                .with(Body.auto<ItemPickup>().toLens() of ItemPickup(user, id, 1))
        )
        return when {
            resp.status.successful -> resultFrom { OrderId.parse(resp.bodyString()) }
            else -> Failure(Exception(resp.status.description))
        }
    }
}

