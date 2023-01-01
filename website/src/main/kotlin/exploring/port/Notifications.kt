package exploring.port

import dev.forkhandles.result4k.Result4k
import org.http4k.connect.amazon.sns.model.PhoneNumber
import java.util.UUID

fun interface Notifications {
    fun collectOrder(user: PhoneNumber, trackingId: UUID): Result4k<UUID, Exception>

    companion object
}
