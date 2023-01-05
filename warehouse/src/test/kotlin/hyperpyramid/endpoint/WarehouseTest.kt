package hyperpyramid.endpoint

import hyperpyramid.TracingTest
import hyperpyramid.Warehouse
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemory
import hyperpyramid.port.Inventory
import org.http4k.client.JavaHttpClient
import java.time.Clock

abstract class WarehouseTest : TracingTest() {
    private val clock = Clock.systemUTC()

    protected val http = Warehouse(
        WarehouseTestEnv, events, clock,
        JavaHttpClient(), Inventory.InMemory(events, clock),
    )
}
