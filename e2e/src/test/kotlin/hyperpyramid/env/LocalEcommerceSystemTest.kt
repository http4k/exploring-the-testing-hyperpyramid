package hyperpyramid.env

import hyperpyramid.EcommerceSystem
import hyperpyramid.TheInternet
import hyperpyramid.actors.HttpWebsiteCustomer
import hyperpyramid.actors.InternetStoreManager
import hyperpyramid.http.LocalhostServiceDiscovery
import hyperpyramid.http.ProxyCallToLiveServerFor
import hyperpyramid.http.start
import hyperpyramid.scenarios.ItemTransactionScenario
import hyperpyramid.setup.setupCloudEnvironment
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random.Default.nextInt


class LocalEcommerceSystemTest : ItemTransactionScenario {
    private val services = LocalhostServiceDiscovery(
        nextInt(9000, 64000),
        "api-gateway", "images", "warehouse", "shop", // cluster services
        "cognito", "dept-store", "email", "s3" // external services
    )

    private val theInternet = TheInternet(services)

    private val http = ProxyCallToLiveServerFor(services)

    private val ecommerceSystem = EcommerceSystem(theInternet.setupCloudEnvironment(), services, http)

    override val customer = HttpWebsiteCustomer(http, services("api-gateway"), theInternet.emailInbox)

    override val storeManager = InternetStoreManager(theInternet)

    @BeforeEach
    fun start() {
        with(theInternet) {
            cognito.start(services, "cognito")
            departmentStore.start(services, "dept-store")
            ses.start(services, "email")
            s3.start(services, "s3")
        }

        with(ecommerceSystem) {
            apiGateway.start(services, "api-gateway")
            images.start(services, "images")
            warehouse.start(services, "warehouse")
            shop.start(services, "shop")
        }
    }
}
