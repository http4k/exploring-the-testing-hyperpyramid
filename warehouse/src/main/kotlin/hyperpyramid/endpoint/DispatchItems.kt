package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.domain.DispatchResult.NoStock
import hyperpyramid.domain.DispatchResult.NotFound
import hyperpyramid.domain.DispatchResult.Sent
import hyperpyramid.domain.Warehouse
import hyperpyramid.dto.ItemPickup
import hyperpyramid.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.PRECONDITION_FAILED
import org.http4k.routing.bind

fun DispatchItems(warehouse: Warehouse) = "/v1/dispatch" bind POST to {
    warehouse.dispatch(Body.auto<ItemPickup>().toLens()(it))
        .map {
            when (it) {
                is Sent -> Response(ACCEPTED).body(it.orderId.toString())
                NotFound -> Response(NOT_FOUND)
                NoStock -> Response(PRECONDITION_FAILED)
            }
        }
        .orThrow()
}

