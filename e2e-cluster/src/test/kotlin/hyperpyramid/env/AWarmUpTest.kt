package hyperpyramid.env

import hyperpyramid.Cluster
import hyperpyramid.TheInternet
import hyperpyramid.actors.Customer
import hyperpyramid.http.ServiceDiscovery
import hyperpyramid.scenarios.LoadStockList
import hyperpyramid.setup.setupCloudEnvironment

class AWarmUpTest : LoadStockList {
    private val services = ServiceDiscovery()

    override val theInternet = TheInternet(services)

    private val cluster = Cluster(theInternet.setupCloudEnvironment(), services, theInternet)

    override val user = Customer(cluster, services("api-gateway"), theInternet.emailInbox)
}
