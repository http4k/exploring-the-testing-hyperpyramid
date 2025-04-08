package hyperpyramid.adapter

import hyperpyramid.WarehouseSettings.DATABASE_DRIVER
import hyperpyramid.WarehouseSettings.DATABASE_URL
import org.h2.Driver
import org.http4k.config.Environment.Companion.defaults

class DatabaseInventoryTest : InventoryContract {
    override val inventory = DatabaseInventory(
        defaults(
            DATABASE_DRIVER of Driver::class.java.name,
            DATABASE_URL of "jdbc:h2:mem:hyperpyramid;DB_CLOSE_DELAY=-1",
        )
    )
}
