package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.ItemId
import exploring.dto.OrderId
import exploring.dto.Phone

fun interface DepartmentStore {
    fun collection(phone: Phone, item: ItemId): Result4k<OrderId, Exception>

    companion object
}
