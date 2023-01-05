package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.LocalhostServiceDiscovery
import exploring.setup.setupCloudEnvironment
import exploring.start
import org.http4k.client.JavaHttpClient
import org.junit.jupiter.api.BeforeEach

class MinimalClusterTest : LoadStockList() {
    override val services = LocalhostServiceDiscovery("api-gateway", "cognito")

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet, events)
    override val http = JavaHttpClient()

    @BeforeEach
    fun start() {
        theInternet.cognito.start(services, "cognito")
        cluster.apiGateway.start(services, "api-gateway")
    }
}
