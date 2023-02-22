package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.TracingTest
import hyperpyramid.Website
import hyperpyramid.WebsiteTestEnv
import hyperpyramid.scenario.BrowsingContract
import org.http4k.filter.debug
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class InMemoryWebsiteTests : TracingTest(), BrowsingContract {
    override val http = Website(WebsiteTestEnv, events, http = FakeWarehouse()).debug()
}