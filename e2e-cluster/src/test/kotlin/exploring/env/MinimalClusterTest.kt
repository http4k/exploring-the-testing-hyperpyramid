package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.LocalhostServiceDiscovery
import exploring.TheInternet
import exploring.actors.Customer
import exploring.setup.setupCloudEnvironment
import exploring.start
import org.http4k.client.JavaHttpClient
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random.Default.nextInt

class MinimalClusterTest : LoadStockList {
    private val services = LocalhostServiceDiscovery(nextInt(9000, 64000), "api-gateway", "cognito")

    override val theInternet = TheInternet(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet)

    override val user = Customer(JavaHttpClient(), services("api-gateway"), theInternet.emailInbox)

    @BeforeEach
    fun start() {
        theInternet.cognito.start(services, "cognito")
        cluster.apiGateway.start(services, "api-gateway")
    }
}
