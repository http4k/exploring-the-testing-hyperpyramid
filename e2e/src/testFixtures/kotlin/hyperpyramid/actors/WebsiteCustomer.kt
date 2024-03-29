package hyperpyramid.actors

import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId

interface WebsiteCustomer : Customer {
    fun canSeeImage(id: ItemId): Boolean
    fun hasEmailFor(orderId: OrderId, email: String): Boolean
}
