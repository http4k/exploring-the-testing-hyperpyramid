package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.Email
import exploring.dto.InventoryItem
import exploring.dto.ItemId
import exploring.dto.OrderId

interface Warehouse {
    fun items(): Result4k<List<InventoryItem>, Exception>
    fun dispatch(user: Email, id: ItemId): Result4k<OrderId, Exception>

    companion object
}

