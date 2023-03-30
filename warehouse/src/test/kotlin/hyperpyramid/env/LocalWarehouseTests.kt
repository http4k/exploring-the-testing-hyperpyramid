package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.WarehouseApi
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemoryInventory
import hyperpyramid.scenario.DispatchContract
import hyperpyramid.scenario.ListItemContract
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class LocalWarehouseTests :
    DispatchContract,
    ListItemContract {

    override val events: Events = {}


    override val http by lazy {
        SetBaseUriFrom(Uri.of("http://localhost:${server.port()}"))
            .then(JavaHttpClient())
    }

    private val server by lazy {
        WarehouseApi(WarehouseTestEnv, http = FakeDepartmentStore(), inventory = InMemoryInventory()).asServer(Undertow(0))
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
