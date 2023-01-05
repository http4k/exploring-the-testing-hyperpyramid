package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.TheInternet
import exploring.actors.Customer
import exploring.http.ServiceDiscovery
import exploring.setup.setupCloudEnvironment

class AWarmUpTest : LoadStockList {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet)

    override val user = Customer(cluster, services("api-gateway"), theInternet.emailInbox)
}
