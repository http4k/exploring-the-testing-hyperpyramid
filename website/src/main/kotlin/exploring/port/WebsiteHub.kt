package exploring.port

import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import exploring.dto.ItemId
import exploring.dto.Phone

class WebsiteHub(private val warehouse: Warehouse, private val notifications: Notifications) {
    fun items() = warehouse.items()

    fun order(phone: Phone, item: ItemId) = warehouse.dispatch(phone, item)
        .flatMap { orderId ->
            notifications.collectOrder(phone, orderId)
                .map { orderId }
        }
}
