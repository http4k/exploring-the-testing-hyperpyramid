package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.ServiceDiscovery
import exploring.TheInternet
import exploring.TracingTest
import exploring.actors.Customer
import exploring.setup.setupCloudEnvironment

class InMemoryClusterTest : TracingTest(), LoadStockList {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet, events)

    override val user = Customer(events, cluster, services("api-gateway"), theInternet.emailInbox)
}

