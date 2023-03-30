package hyperpyramid.port

import dev.forkhandles.result4k.Result4k
import hyperpyramid.dto.Email
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId

interface Warehouse {
    fun items(): Result4k<List<InventoryItem>, Exception>
    fun dispatch(user: Email, id: ItemId): Result4k<OrderId, Exception>

    companion object
}

