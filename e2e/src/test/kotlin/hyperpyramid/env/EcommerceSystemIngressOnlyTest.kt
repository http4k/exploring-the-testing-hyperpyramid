package hyperpyramid.env

import hyperpyramid.EcommerceSystem
import hyperpyramid.TheInternet
import hyperpyramid.actors.Customer
import hyperpyramid.http.LocalhostServiceDiscovery
import hyperpyramid.http.start
import hyperpyramid.scenarios.LoadStockList
import hyperpyramid.setup.setupCloudEnvironment
import org.http4k.client.JavaHttpClient
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random.Default.nextInt

class EcommerceSystemIngressOnlyTest : LoadStockList {
    private val services = LocalhostServiceDiscovery(nextInt(9000, 64000), "api-gateway", "cognito")

    override val theInternet = TheInternet(services)

    private val ecommerceSystem = EcommerceSystem(theInternet.setupCloudEnvironment(), services, theInternet)

    override val user = Customer(JavaHttpClient(), services("api-gateway"), theInternet.emailInbox)

    @BeforeEach
    fun start() {
        theInternet.cognito.start(services, "cognito")
        ecommerceSystem.apiGateway.start(services, "api-gateway")
    }
}
