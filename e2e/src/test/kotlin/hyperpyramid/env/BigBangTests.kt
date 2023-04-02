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
import org.junit.jupiter.api.Disabled

@Disabled("These tests are designed to show what happens on failure!")
class BigBangTests : RecordTraces(), CustomerBuysItemScenario {
    val clock = TestClock
    val iNet = TheInternet().apply {
        departmentStore.misbehave()
    }

    val env = UniverseTestEnv overrides iNet.createCloudResourcesAndEnv()

    val system = EcommerceSystem(env, clock, events, iNet)

    override val customer = HttpWebsiteCustomer(env[API_GATEWAY_URL], clock, events, system, iNet.emails)
    override val storeManager = InternetStoreManager(iNet)
}

