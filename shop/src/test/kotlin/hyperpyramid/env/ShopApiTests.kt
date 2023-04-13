package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.RecordTraces
import hyperpyramid.ShopApi
import hyperpyramid.ShopTestEnv
import hyperpyramid.TestClock
import hyperpyramid.actors.HttpCustomer
import hyperpyramid.scenario.ListItemsScenario
import org.http4k.core.Uri
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class ShopApiTests : RecordTraces(), ListItemsScenario {
    val http = ShopApi(ShopTestEnv, TestClock, events, FakeWarehouse())
    override val customer = HttpCustomer(Uri.of("http://shop"), TestClock, events, http)
}
