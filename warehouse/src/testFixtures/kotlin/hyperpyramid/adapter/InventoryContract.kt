package hyperpyramid.adapter

import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.port.Inventory
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import java.util.UUID

interface InventoryContract {
    val inventory: Inventory

    @Test
    fun `item lifecycle`() {
        val item = InventoryItem(ItemId.of(UUID.randomUUID().toString()), "foobar", 1)
        with(inventory) {
            store(item).orThrow()
            expectThat(items().orThrow()).contains(item)
            adjust(item.id, 1).orThrow()
            expectThat(items().orThrow()).contains(item.copy(stock = 0))
        }
    }
}
