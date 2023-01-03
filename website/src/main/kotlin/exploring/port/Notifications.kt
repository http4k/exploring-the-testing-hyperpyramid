package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.Phone
import exploring.dto.PickupId

fun interface Notifications {
    fun collectOrder(phone: Phone, trackingId: PickupId): Result4k<Unit, Exception>

    companion object
}
