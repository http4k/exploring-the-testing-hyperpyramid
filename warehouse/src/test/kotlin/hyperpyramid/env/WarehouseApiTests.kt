package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.RecordTraces
import hyperpyramid.TestClock
import hyperpyramid.WarehouseApi
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemoryInventory
import hyperpyramid.scenario.DispatchScenario
import hyperpyramid.scenario.ListItemScenario
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class WarehouseApiTests : RecordTraces(),
    DispatchScenario,
    ListItemScenario {

    override val http = WarehouseApi(
        WarehouseTestEnv,
        TestClock,
        events,
        FakeDepartmentStore(),
        InMemoryInventory(events)
    )
}
