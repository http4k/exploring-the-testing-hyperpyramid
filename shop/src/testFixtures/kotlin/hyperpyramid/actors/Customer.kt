package hyperpyramid.actors

import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId

interface Customer {
    fun login()
    fun listItems(): List<ItemId>
    fun canSeeImage(id: ItemId): Boolean
    fun order(id: ItemId): OrderId
    fun hasEmailFor(orderId: OrderId): Boolean
}
