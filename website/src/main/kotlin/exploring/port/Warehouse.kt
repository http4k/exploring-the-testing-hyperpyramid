package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.InventoryItem
import exploring.dto.ItemId
import exploring.dto.OrderId
import exploring.dto.Phone

interface Warehouse {
    fun items(): Result4k<List<InventoryItem>, Exception>
    fun dispatch(phone: Phone, id: ItemId): Result4k<OrderId, Exception>

    companion object
}

