package exploring.env

import exploring.Cluster
import exploring.LoadStockList
import exploring.ServiceDiscovery
import exploring.setup.setupCloudEnvironment

class InMemoryClusterTest : LoadStockList() {
    override val services = ServiceDiscovery()

    override val http by lazy {
        Cluster(theInternet.setupCloudEnvironment(), services, theInternet, events)
    }
}

