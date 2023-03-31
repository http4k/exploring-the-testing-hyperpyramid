package hyperpyramid.actors

import hyperpyramid.TheInternet
import hyperpyramid.dto.OrderId

fun InternetStoreManager(theInternet: TheInternet) = object : StoreManager {
    override fun hasOrderItems(orderId: OrderId) = theInternet.departmentStore.orders[orderId]?.items
}
