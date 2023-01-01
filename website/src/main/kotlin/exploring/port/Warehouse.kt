package exploring.port

import dev.forkhandles.result4k.Result4k
import exploring.dto.InventoryItem
import java.util.UUID

interface Warehouse {
    fun items(): Result4k<List<InventoryItem>, Exception>
    fun dispatch(id: UUID): Result4k<UUID, Exception>

    companion object
}

