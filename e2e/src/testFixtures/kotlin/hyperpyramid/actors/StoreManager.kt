package hyperpyramid.actors

import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId

interface StoreManager {
    fun hasOrderItems(orderId: OrderId): List<ItemId>?
}
