package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.ItemId
import exploring.dto.Order
import exploring.dto.OrderId
import exploring.dto.Phone

interface DepartmentStore {
    fun collection(phone: Phone, item: ItemId): Result4k<OrderId, Exception>
    fun lookup(orderId: OrderId): Result4k<Order?, Exception>

    companion object
}
