package hyperpyramid.port

import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Result4k
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId

interface Inventory {
    fun items(): Result4k<List<InventoryItem>, Exception>
    fun store(item: InventoryItem): Result4k<Unit, Exception>
    fun adjust(id: ItemId, amount: Int): Result<Unit?, Exception>

    companion object
}
