package hyperpyramid.actors

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.Email
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import hyperpyramid.port.Shop

fun DomainCustomer(
    user: Email,
    shop: Shop
) = object : Customer {
    override fun login() {}

    override fun listItems() = shop.items().map { it.map(InventoryItem::id) }.orThrow()

    override fun canSeeImage(id: ItemId) = true

    override fun order(id: ItemId) = shop.order(user, id).orThrow()

    override fun hasEmailFor(orderId: OrderId) = true
}
