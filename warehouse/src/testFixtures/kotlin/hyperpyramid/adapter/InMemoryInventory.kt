package hyperpyramid.adapter

import hyperpyramid.app.AppEvents
import hyperpyramid.app.DbCall
import hyperpyramid.dto.InventoryItem
import hyperpyramid.dto.ItemId
import hyperpyramid.port.Inventory
import org.http4k.connect.storage.InMemory
import org.http4k.connect.storage.Storage
import org.http4k.events.Events
import org.http4k.filter.ZipkinTracesStorage.Companion.THREAD_LOCAL
import org.http4k.filter.inChildSpan
import java.lang.reflect.Proxy.newProxyInstance
import java.time.Clock

fun InMemoryInventory(events: Events = {}, clock: Clock = Clock.systemUTC()): Inventory {
    val target = StorageInventory(Storage.InMemory())
    target.apply {
        store(InventoryItem(ItemId.of("1"), "Banana", 5))
        store(InventoryItem(ItemId.of("2"), "Bottom", 0))
        store(InventoryItem(ItemId.of("3"), "Minion Toys", 100))
        store(InventoryItem(ItemId.of("4"), "Guitar", 12))
    }

    return newProxyInstance(Inventory::class.java.classLoader, arrayOf(Inventory::class.java)) { _, m, args ->
        THREAD_LOCAL.inChildSpan {
            AppEvents("warehouse", clock, events)(DbCall(m.name))
            m(target, *args ?: arrayOf())
        }
    } as Inventory
}

