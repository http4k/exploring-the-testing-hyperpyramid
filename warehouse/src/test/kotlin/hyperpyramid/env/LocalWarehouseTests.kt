package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.TestClock
import hyperpyramid.WarehouseApi
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemoryInventory
import hyperpyramid.scenario.DispatchScenario
import hyperpyramid.scenario.ListItemScenario
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class LocalWarehouseTests :
    DispatchScenario,
    ListItemScenario {

    override val events: Events = {}

    override val http by lazy {
        SetHostFrom(Uri.of("http://localhost:${server.port()}"))
            .then(JavaHttpClient())
    }

    private val server by lazy {
        WarehouseApi(WarehouseTestEnv, TestClock, events, FakeDepartmentStore(), InMemoryInventory())
            .asServer(Undertow(0))
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
