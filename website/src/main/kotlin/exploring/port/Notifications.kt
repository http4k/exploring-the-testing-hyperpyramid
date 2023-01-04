package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.Email
import exploring.dto.OrderId

fun interface Notifications {
    fun collectOrder(user: Email, orderId: OrderId): Result4k<Unit, Exception>

    companion object
}
