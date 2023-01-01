package exploring.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import exploring.dto.DispatchRequest
import exploring.port.DispatchResult.NoStock
import exploring.port.DispatchResult.NotFound
import exploring.port.DispatchResult.Sent
import exploring.port.WarehouseHub
import exploring.util.Json.auto
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.PRECONDITION_FAILED
import org.http4k.routing.bind

fun DispatchItems(hub: WarehouseHub) = "/v1/dispatch" bind POST to {
    hub.dispatch(Body.auto<DispatchRequest>().toLens()(it))
        .map {
            when (it) {
                is Sent -> Response(OK).body(it.trackingNumber.toString())
                NotFound -> Response(NOT_FOUND)
                NoStock -> Response(PRECONDITION_FAILED)
            }
        }
        .orThrow()
}

