package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.LocalhostServiceDiscovery
import exploring.TheInternet
import exploring.TracingTest
import exploring.actors.Customer
import exploring.setup.setupCloudEnvironment
import exploring.start
import org.http4k.client.JavaHttpClient
import org.junit.jupiter.api.BeforeEach

class MinimalClusterTest : TracingTest(), LoadStockList {
    private val services = LocalhostServiceDiscovery("api-gateway", "cognito")

    override val theInternet = TheInternet(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet, events)

    override val user = Customer(events, JavaHttpClient(), services("api-gateway"), theInternet.emailInbox)

    @BeforeEach
    fun start() {
        theInternet.cognito.start(services, "cognito")
        cluster.apiGateway.start(services, "api-gateway")
    }
}
