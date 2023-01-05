package hyperpyramid.adapter

import hyperpyramid.WarehouseSettings.DATABASE_DRIVER
import hyperpyramid.WarehouseSettings.DATABASE_URL
import hyperpyramid.port.Inventory
import org.h2.Driver
import org.http4k.cloudnative.env.Environment.Companion.defaults

class DatabaseInventoryTest : InventoryContract {
    override val inventory = Inventory.Database(
        defaults(
            DATABASE_DRIVER of Driver::class.java.name,
            DATABASE_URL of "jdbc:h2:mem:hyperpyramid;DB_CLOSE_DELAY=-1",
        )
    )
}
