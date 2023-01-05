package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.TheInternet
import exploring.actors.Customer
import exploring.http.LocalhostServiceDiscovery
import exploring.http.ProxyCallToLiveServerFor
import exploring.http.start
import exploring.setup.setupCloudEnvironment
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random.Default.nextInt


class PortBoundClusterTest : LoadStockList {
    private val services = LocalhostServiceDiscovery(
        nextInt(9000, 64000),
        "api-gateway", "images", "warehouse", "website", // cluster services
        "cognito", "dept-store", "email", "s3" // external services
    )

    override val theInternet = TheInternet(services)

    private val http = ProxyCallToLiveServerFor(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, http)

    override val user = Customer(http, services("api-gateway"), theInternet.emailInbox)

    @BeforeEach
    fun start() {
        with(theInternet) {
            cognito.start(services, "cognito")
            departmentStore.start(services, "dept-store")
            ses.start(services, "email")
            s3.start(services, "s3")
        }

        with(cluster) {
            apiGateway.start(services, "api-gateway")
            images.start(services, "images")
            warehouse.start(services, "warehouse")
            website.start(services, "website")
        }
    }
}
