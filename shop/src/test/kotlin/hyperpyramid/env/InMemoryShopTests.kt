package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.RecordTraces
import hyperpyramid.ShopApi
import hyperpyramid.ShopTestEnv
import hyperpyramid.scenario.BrowsingContract
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class InMemoryShopTests : RecordTraces(), BrowsingContract {
    override val http = ShopApi(ShopTestEnv, events, http = FakeWarehouse())
}
