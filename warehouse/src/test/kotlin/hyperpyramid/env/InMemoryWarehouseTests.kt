package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.TracingTest
import hyperpyramid.WarehouseApi
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemoryInventory
import hyperpyramid.scenario.DispatchContract
import hyperpyramid.scenario.ListItemContract
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class InMemoryWarehouseTests : TracingTest(),
    DispatchContract,
    ListItemContract {

    override val http = WarehouseApi(
        WarehouseTestEnv, events = events, http = FakeDepartmentStore(), inventory = InMemoryInventory(events)
    )
}
