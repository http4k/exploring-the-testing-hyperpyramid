package hyperpyramid.adapter

import hyperpyramid.app.AppEvents
import hyperpyramid.app.DbCall
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.port.Inventory
import org.http4k.connect.storage.InMemory
import org.http4k.connect.storage.Storage
import org.http4k.events.Events
import java.lang.reflect.Proxy.newProxyInstance
import java.time.Clock

fun Inventory.Companion.InMemory(events: Events, clock: Clock): Inventory {
    val target = Inventory.Storage(Storage.InMemory())
    target.apply {
        store(InventoryItem(ItemId.of("1"), "Banana", 5))
        store(InventoryItem(ItemId.of("2"), "Bottom", 0))
        store(InventoryItem(ItemId.of("3"), "Minion Toys", 100))
        store(InventoryItem(ItemId.of("4"), "Guitar", 12))
    }

    return newProxyInstance(Inventory::class.java.classLoader, arrayOf(Inventory::class.java)) { _, m, args ->
        DbCall(m.name)
        AppEvents("warehouse", clock, events)(DbCall(m.name))
        m.invoke(target, *args ?: arrayOf())
    } as Inventory
}

