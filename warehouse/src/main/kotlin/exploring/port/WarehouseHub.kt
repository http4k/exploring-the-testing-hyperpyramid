package exploring.port

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import exploring.dto.DispatchRequest
import exploring.port.DispatchResult.NoStock
import exploring.port.DispatchResult.NotFound
import exploring.port.DispatchResult.Sent
import java.util.UUID

class WarehouseHub(
    private val inventory: Inventory,
    private val dispatch: Dispatcher
) {
    fun items() = inventory.items()

    fun dispatch(dispatchRequest: DispatchRequest): Result4k<DispatchResult, Exception> =
        inventory.items()
            .map { it.firstOrNull { it.id == dispatchRequest.id } }
            .flatMap {
                when {
                    it == null -> Success(NotFound)
                    it.stock < dispatchRequest.amount -> Success(NoStock)
                    else -> inventory.adjust(dispatchRequest.id, dispatchRequest.amount)
                        .flatMap {
                            when (it) {
                                null -> Success(NotFound)
                                else -> dispatch.dispatch(dispatchRequest).map { Sent(it) }
                            }
                        }
                }
            }
}

sealed interface DispatchResult {
    data class Sent(val trackingNumber: UUID) : DispatchResult
    object NoStock : DispatchResult
    object NotFound : DispatchResult
}
