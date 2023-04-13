package hyperpyramid.actors

import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId

interface Customer {
    fun listItems(): List<ItemId>
    fun order(id: ItemId, email: String): OrderId
}
