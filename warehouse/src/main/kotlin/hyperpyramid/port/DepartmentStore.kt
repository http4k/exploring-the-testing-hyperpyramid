package hyperpyramid.port

import dev.forkhandles.result4k.Result4k
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.Order
import hyperpyramid.dto.OrderId

interface DepartmentStore {
    fun collection(user: Email, item: ItemId): Result4k<OrderId, Exception>
    fun lookup(orderId: OrderId): Result4k<Order?, Exception>

    companion object
}
