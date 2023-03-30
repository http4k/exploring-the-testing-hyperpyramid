package hyperpyramid.env

import hyperpyramid.EcommerceSystem
import hyperpyramid.TheInternet
import hyperpyramid.TracingTest
import hyperpyramid.actors.HttpCustomer
import hyperpyramid.http.ServiceDiscovery
import hyperpyramid.scenarios.ItemTransactionScenario
import hyperpyramid.setup.setupCloudEnvironment

class InMemoryEcommerceSystemTest : TracingTest(), ItemTransactionScenario {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val ecommerceSystem = EcommerceSystem(theInternet.setupCloudEnvironment(), services, theInternet, events)

    override val customer = HttpCustomer(ecommerceSystem, services("api-gateway"), theInternet.emailInbox, events)
}

