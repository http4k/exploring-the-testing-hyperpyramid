package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.ShopApi
import hyperpyramid.TracingTest
import hyperpyramid.WebsiteTestEnv
import hyperpyramid.scenario.BrowsingContract
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class InMemoryShopTests : TracingTest(), BrowsingContract {
    override val http = ShopApi(WebsiteTestEnv, events, http = FakeWarehouse())
}
