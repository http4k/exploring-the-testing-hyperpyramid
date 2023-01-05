package hyperpyramid.env

import hyperpyramid.FakeDepartmentStore
import hyperpyramid.Warehouse
import hyperpyramid.WarehouseTestEnv
import hyperpyramid.adapter.InMemory
import hyperpyramid.port.Inventory
import hyperpyramid.scenario.DispatchContract
import hyperpyramid.scenario.ListItemContract
import org.http4k.client.JavaHttpClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.testing.ApprovalTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class ServerWarehouseTests :
    DispatchContract,
    ListItemContract {

    override val http by lazy {
        SetBaseUriFrom(Uri.of("http://localhost:${server.port()}"))
            .then(JavaHttpClient())
    }

    private val server by lazy {
        Warehouse(WarehouseTestEnv, FakeDepartmentStore(), Inventory.InMemory()).asServer(SunHttp(0))
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
