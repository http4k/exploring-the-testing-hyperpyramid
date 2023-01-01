package exploring.adapter

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.resultFrom
import exploring.dto.DispatchRequest
import exploring.dto.InventoryItem
import exploring.dto.ItemId
import exploring.port.Warehouse
import exploring.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters.SetHostFrom
import java.util.UUID

fun Warehouse.Companion.Http(rawHttp: HttpHandler) = object : Warehouse {
    private val http = SetHostFrom(Uri.of("http://warehouse")).then(rawHttp)

    override fun items(): Result4k<List<InventoryItem>, Exception> {
        val resp = http(Request(GET, "/v1/items"))
        return when {
            resp.status.successful -> Success(Body.auto<List<InventoryItem>>().toLens()(resp))
            else -> Failure(Exception("oh no!"))
        }
    }

    override fun dispatch(id: ItemId): Result4k<UUID, Exception> {
        val resp = http(
            Request(POST, "/v1/dispatch")
                .with(Body.auto<DispatchRequest>().toLens() of DispatchRequest(id, 1))
        )
        return when {
            resp.status.successful -> resultFrom { UUID.fromString(resp.bodyString()) }
            else -> Failure(Exception("oh no!"))
        }
    }
}

