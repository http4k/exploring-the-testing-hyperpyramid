package hyperpyramid.env

import hyperpyramid.ApiGatewaySettings.API_GATEWAY_URL
import hyperpyramid.EcommerceSystem
import hyperpyramid.RecordTraces
import hyperpyramid.TestClock
import hyperpyramid.TheInternet
import hyperpyramid.actors.HttpWebsiteCustomer
import hyperpyramid.actors.InternetStoreManager
import hyperpyramid.scenarios.CustomerBuysItemScenario
import hyperpyramid.setup.createCloudResourcesAndEnv

class UniverseTests : RecordTraces(), CustomerBuysItemScenario {
    val iNet = TheInternet()

    val env = UniverseTestEnv overrides iNet.createCloudResourcesAndEnv()

    val system = EcommerceSystem(env, TestClock, events, iNet)

    override val customer = HttpWebsiteCustomer(env[API_GATEWAY_URL], TestClock, events, system, iNet.emails)
    override val storeManager = InternetStoreManager(iNet)
}
