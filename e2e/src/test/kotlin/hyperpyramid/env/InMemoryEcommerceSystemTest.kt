package hyperpyramid.env

import hyperpyramid.EcommerceSystem
import hyperpyramid.TheInternet
import hyperpyramid.TracingTest
import hyperpyramid.actors.HttpCustomer
import hyperpyramid.http.ServiceDiscovery
import hyperpyramid.scenarios.LoadStockList
import hyperpyramid.setup.setupCloudEnvironment

class InMemoryEcommerceSystemTest : TracingTest(), LoadStockList {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val ecommerceSystem = EcommerceSystem(theInternet.setupCloudEnvironment(), services, theInternet, events)

    override val user = HttpCustomer(ecommerceSystem, services("api-gateway"), theInternet.emailInbox, events)
}

