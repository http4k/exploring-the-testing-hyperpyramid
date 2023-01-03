package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.InventoryItem
import exploring.dto.ItemId
import exploring.dto.Phone
import exploring.dto.PickupId

interface Warehouse {
    fun items(): Result4k<List<InventoryItem>, Exception>
    fun dispatch(phone: Phone, id: ItemId): Result4k<PickupId, Exception>

    companion object
}

