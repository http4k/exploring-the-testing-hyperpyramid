package hyperpyramid.adapter

import dev.forkhandles.result4k.resultFrom
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.port.Inventory
import org.http4k.connect.storage.Storage

fun Inventory.Companion.Storage(storage: Storage<InventoryItem>) = object : Inventory {
    override fun items() = resultFrom { storage.keySet("").map { storage[it]!! } }

    override fun store(item: InventoryItem) = resultFrom { storage[item.id.value] = item }

    override fun adjust(id: ItemId, amount: Int) = resultFrom {
        storage[id.value]
            ?.let { it.copy(stock = it.stock - amount) }
            ?.let {
                when (it.stock) {
                    0 -> storage[id.value] = it
                    else -> storage[id.value] = it
                }
            }
    }
}
