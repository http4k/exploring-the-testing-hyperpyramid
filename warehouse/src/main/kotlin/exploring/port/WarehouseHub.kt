package exploring.port

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import exploring.dto.ItemPickup
import exploring.dto.PickupId
import exploring.port.DispatchResult.NoStock
import exploring.port.DispatchResult.NotFound
import exploring.port.DispatchResult.Sent

class WarehouseHub(
    private val inventory: Inventory,
    private val dispatch: Dispatcher
) {
    fun items() = inventory.items()

    fun dispatch(itemPickup: ItemPickup): Result4k<DispatchResult, Exception> =
        inventory.items()
            .map { it.firstOrNull { it.id == itemPickup.id } }
            .flatMap {
                when {
                    it == null -> Success(NotFound)
                    it.stock < itemPickup.amount -> Success(NoStock)
                    else -> inventory.adjust(itemPickup.id, itemPickup.amount)
                        .flatMap {
                            when (it) {
                                null -> Success(NotFound)
                                else -> dispatch.dispatch(itemPickup).map { Sent(it) }
                            }
                        }
                }
            }
}

sealed interface DispatchResult {
    data class Sent(val pickupId: PickupId) : DispatchResult
    object NoStock : DispatchResult
    object NotFound : DispatchResult
}
