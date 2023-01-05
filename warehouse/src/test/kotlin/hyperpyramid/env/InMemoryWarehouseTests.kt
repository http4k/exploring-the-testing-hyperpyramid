package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.TracingTest
import hyperpyramid.Warehouse
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.actor.WarehouseClient
import hyperpyramid.adapter.InMemory
import hyperpyramid.port.Inventory
import hyperpyramid.scenario.DispatchContract
import hyperpyramid.scenario.ListItemContract
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock

@ExtendWith(ApprovalTest::class)
class InMemoryWarehouseTests : TracingTest(),
    DispatchContract,
    ListItemContract {
    private val clock = Clock.systemUTC()

    private val http = Warehouse(
        WarehouseTestEnv, FakeDepartmentStore(), Inventory.InMemory(events, clock),
        events, clock,
    )

    override fun client() = WarehouseClient(http, events)
}
