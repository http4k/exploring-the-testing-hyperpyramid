package hyperpyramid.port

import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import hyperpyramid.app.CustomerOrder
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import org.http4k.events.Events

class Shop(private val events: Events,
           private val warehouse: Warehouse,
           private val notifications: Notifications) {
    fun items() = warehouse.items()

    fun order(user: Email, item: ItemId) = warehouse.dispatch(user, item)
        .flatMap { orderId ->
            notifications.collectOrder(user, orderId)
                .map { orderId }
                .also { events(CustomerOrder(item)) }
        }
}
