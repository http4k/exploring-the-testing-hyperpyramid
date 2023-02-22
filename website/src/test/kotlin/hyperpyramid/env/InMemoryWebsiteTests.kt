package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.TracingTest
import hyperpyramid.Website
import hyperpyramid.WebsiteTestEnv
import hyperpyramid.scenario.BrowsingContract
import org.http4k.events.then
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class InMemoryWebsiteTests : TracingTest(), BrowsingContract {

    override val http = Website(WebsiteTestEnv, events.then(::println), http = FakeWarehouse())
}
