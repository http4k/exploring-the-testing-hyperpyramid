package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.Website
import hyperpyramid.WebsiteTestEnv
import hyperpyramid.scenario.BrowsingContract
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class LocalWebsiteTests : BrowsingContract {

    override val http by lazy {
        SetBaseUriFrom(Uri.of("http://localhost:${server.port()}"))
            .then(JavaHttpClient())
    }

    private val server by lazy {
        Website(WebsiteTestEnv, http = FakeWarehouse()).asServer(Undertow(0))
    }

    @BeforeEach
    fun start() {
        server.start()
    }

    @AfterEach
    fun stop() {
        server.stop()
    }
}