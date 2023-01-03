package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.OrderId
import exploring.dto.Phone

fun interface Notifications {
    fun collectOrder(phone: Phone, trackingId: OrderId): Result4k<Unit, Exception>

    companion object
}
