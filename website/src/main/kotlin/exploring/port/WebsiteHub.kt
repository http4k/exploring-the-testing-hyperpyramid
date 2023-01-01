package exploring.port

import dev.forkhandles.result4k.flatMap
import exploring.dto.ItemId
import org.http4k.connect.amazon.sns.model.PhoneNumber

class WebsiteHub(private val warehouse: Warehouse, private val notifications: Notifications) {
    fun items() = warehouse.items()

    fun order(user: PhoneNumber, item: ItemId) = warehouse.dispatch(item)
        .flatMap { notifications.collectOrder(user, it) }
}
