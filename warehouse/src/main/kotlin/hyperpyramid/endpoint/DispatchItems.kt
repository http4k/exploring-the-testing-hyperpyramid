package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.ItemPickup
import hyperpyramid.port.DispatchResult.NoStock
import hyperpyramid.port.DispatchResult.NotFound
import hyperpyramid.port.DispatchResult.Sent
import hyperpyramid.port.WarehouseHub
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.PRECONDITION_FAILED
import org.http4k.routing.bind

fun DispatchItems(hub: WarehouseHub) = "/v1/dispatch" bind POST to {
    hub.dispatch(Body.auto<ItemPickup>().toLens()(it))
        .map {
            when (it) {
                is Sent -> Response(ACCEPTED).body(it.orderId.toString())
                NotFound -> Response(NOT_FOUND)
                NoStock -> Response(PRECONDITION_FAILED)
            }
        }
        .orThrow()
}

