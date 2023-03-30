package hyperpyramid.env

import hyperpyramid.EcommerceSystem
import hyperpyramid.TheInternet
import hyperpyramid.TracingTest
import hyperpyramid.actors.Customer
import hyperpyramid.http.ServiceDiscovery
import hyperpyramid.scenarios.LoadStockList
import hyperpyramid.setup.setupCloudEnvironment

class InMemoryEcommerceSystemTest : TracingTest(), LoadStockList {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val ecommerceSystem = EcommerceSystem(theInternet.setupCloudEnvironment(), services, theInternet, events)

    override val user = Customer(ecommerceSystem, services("api-gateway"), theInternet.emailInbox, events)
}

