package hyperpyramid.endpoint

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.TracingTest
import hyperpyramid.Warehouse
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.actor.WarehouseClient
import hyperpyramid.adapter.InMemory
import hyperpyramid.port.Inventory
import java.time.Clock

abstract class WarehouseTest : TracingTest() {
    private val clock = Clock.systemUTC()

    private val http = Warehouse(
        WarehouseTestEnv, events, clock,
        FakeDepartmentStore(), Inventory.InMemory(events, clock),
    )

    val client = WarehouseClient(http, events)
}
