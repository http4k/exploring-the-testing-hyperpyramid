package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.TheInternet
import exploring.TracingTest
import exploring.actors.Customer
import exploring.http.ServiceDiscovery
import exploring.setup.setupCloudEnvironment

class InMemoryClusterTest : TracingTest(), LoadStockList {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet, events)

    override val user = Customer(cluster, services("api-gateway"), theInternet.emailInbox, events)
}

