package hyperpyramid.adapter

import java.time.Clock

class InMemoryInventoryTest : InventoryContract {
    override val inventory = InMemoryInventory({}, Clock.systemUTC())
}
