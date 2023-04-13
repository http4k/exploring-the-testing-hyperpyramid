package hyperpyramid.env

import hyperpyramid.FakeWarehouse
import hyperpyramid.RecordTraces
import hyperpyramid.ShopApi
import hyperpyramid.ShopTestEnv
import hyperpyramid.TestClock
import hyperpyramid.actors.HttpCustomer
import hyperpyramid.scenario.ListItemsScenario
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

/**
 * This is running the same scenarios as the in-memory version, but is starting up the Shop on a port.
 */
@ExtendWith(ApprovalTest::class)
class LocalShopApiTests : RecordTraces(), ListItemsScenario {

    override val customer by lazy {
        val http = SetHostFrom(Uri.of("http://localhost:${server.port()}"))
            .then(JavaHttpClient())

        HttpCustomer(Uri.of("http://shop"), TestClock, events, http)
    }

    private val server by lazy {
        ShopApi(ShopTestEnv, TestClock, events, FakeWarehouse()).asServer(Undertow(0))
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
