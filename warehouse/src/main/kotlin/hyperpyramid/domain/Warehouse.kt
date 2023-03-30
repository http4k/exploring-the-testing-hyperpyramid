package hyperpyramid.domain

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import hyperpyramid.domain.DispatchResult.NoStock
import hyperpyramid.domain.DispatchResult.NotFound
import hyperpyramid.domain.DispatchResult.Sent
import hyperpyramid.dto.ItemPickup
import hyperpyramid.dto.OrderId
import hyperpyramid.port.DepartmentStore
import hyperpyramid.port.Inventory

class Warehouse(
    private val inventory: Inventory,
    private val departmentStore: DepartmentStore
) {
    fun items() = inventory.items().map { it.filter { it.stock > 0 } }

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
                                else -> departmentStore.collection(itemPickup.customer, itemPickup.id)
                                    .map { Sent(it) }
                            }
                        }
                }
            }
}

sealed interface DispatchResult {
    data class Sent(val orderId: OrderId) : DispatchResult
    object NoStock : DispatchResult
    object NotFound : DispatchResult
}
