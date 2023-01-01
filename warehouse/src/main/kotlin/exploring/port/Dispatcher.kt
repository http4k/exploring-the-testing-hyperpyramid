package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.DispatchRequest
import java.util.UUID

fun interface Dispatcher {
    fun dispatch(dispatchRequest: DispatchRequest): Result4k<UUID, Exception>

    companion object
}

