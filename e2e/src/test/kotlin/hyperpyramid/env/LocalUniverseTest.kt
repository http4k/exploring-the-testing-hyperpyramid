package hyperpyramid.env

import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.ApiGatewaySettings.IMAGES_URL
import hyperpyramid.ApiGatewaySettings.SHOP_URL
import hyperpyramid.EcommerceSystem
import hyperpyramid.RecordTraces
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.TheInternet
import hyperpyramid.WarehouseSettings.STORE_URL
import hyperpyramid.actors.HttpWebsiteCustomer
import hyperpyramid.actors.InternetStoreManager
import hyperpyramid.scenarios.CustomerBuysItemScenario
import hyperpyramid.setup.createCloudResourcesAndEnv
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.filter.debug
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.BeforeEach
import java.time.Clock


class LocalUniverseTest : RecordTraces(), CustomerBuysItemScenario {

    private val http = JavaHttpClient()
    private val theInternet = TheInternet()
    private val clock = Clock.systemUTC()

    private val env = LocalUniverseTestEnv overrides theInternet.createCloudResourcesAndEnv()

    private val ecommerceSystem = EcommerceSystem(env, clock, events, http.debug())

    override val customer = HttpWebsiteCustomer(env[API_GATEWAY_URL], clock, events, http, theInternet.emails)

    override val storeManager = InternetStoreManager(theInternet)

    @BeforeEach
    fun start() {
        with(ecommerceSystem) {
            apiGateway.start(env[API_GATEWAY_URL])
            images.start(env[IMAGES_URL])
            shop.start(env[SHOP_URL])
            warehouse.start(env[WAREHOUSE_URL])
        }

        // we can reuse the store port for all of the external traffic
        theInternet.start(env[STORE_URL])
    }
}

private fun HttpHandler.start(uri: Uri) = asServer(SunHttp(uri.port!!))

