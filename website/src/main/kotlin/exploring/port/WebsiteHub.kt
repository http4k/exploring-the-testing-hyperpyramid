package exploring.port

import dev.forkhandles.result4k.flatMap
import exploring.dto.ItemId
import exploring.dto.Phone

class WebsiteHub(private val warehouse: Warehouse, private val notifications: Notifications) {
    fun items() = warehouse.items()

    fun order(phone: Phone, item: ItemId) = warehouse.dispatch(phone, item)
        .flatMap { notifications.collectOrder(phone, it) }
}
