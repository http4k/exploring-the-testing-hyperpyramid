package exploring.port

import dev.forkhandles.result4k.flatMap
import org.http4k.connect.amazon.sns.model.PhoneNumber
import java.util.UUID

class WebsiteHub(private val warehouse: Warehouse, private val notifications: Notifications) {
    fun items() = warehouse.items()

    fun order(user: PhoneNumber, item: UUID) = warehouse.dispatch(item)
        .flatMap { notifications.collectOrder(user, it) }
}
