package hyperpyramid.port

import dev.forkhandles.result4k.Result4k
import hyperpyramid.dto.Email
import hyperpyramid.dto.OrderId

fun interface Notifications {
    fun collectOrder(user: Email, orderId: OrderId): Result4k<Unit, Exception>

    companion object
}
