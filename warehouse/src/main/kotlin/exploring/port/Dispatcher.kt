package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.ItemPickup
import exploring.dto.PickupId

fun interface Dispatcher {
    fun dispatch(itemPickup: ItemPickup): Result4k<PickupId, Exception>

    companion object
}

