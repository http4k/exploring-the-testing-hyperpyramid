package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.Email
import exploring.dto.ItemId
import exploring.dto.Order
import exploring.dto.OrderId

interface DepartmentStore {
    fun collection(user: Email, item: ItemId): Result4k<OrderId, Exception>
    fun lookup(orderId: OrderId): Result4k<Order?, Exception>

    companion object
}
