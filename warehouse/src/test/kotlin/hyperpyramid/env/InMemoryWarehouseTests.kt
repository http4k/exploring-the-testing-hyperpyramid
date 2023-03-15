package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.TracingTest
import hyperpyramid.Warehouse
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemory
import hyperpyramid.port.Inventory
import hyperpyramid.scenario.DispatchContract
import hyperpyramid.scenario.ListItemContract
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class InMemoryWarehouseTests : TracingTest(),
    DispatchContract,
    ListItemContract {

    override val http = Warehouse(
        WarehouseTestEnv, events = events, http = FakeDepartmentStore(), inventory = Inventory.InMemory(events)
    )
}
