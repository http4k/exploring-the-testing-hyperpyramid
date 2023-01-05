package hyperpyramid.adapter

import hyperpyramid.port.Inventory
import java.time.Clock

class InMemoryInventoryTest : InventoryContract {
    override val inventory = Inventory.InMemory({}, Clock.systemUTC())
}
