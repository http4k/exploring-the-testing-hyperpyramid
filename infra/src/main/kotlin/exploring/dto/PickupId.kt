package exploring.dto

import dev.forkhandles.values.UUIDValue
import dev.forkhandles.values.UUIDValueFactory
import java.util.UUID

class PickupId private constructor(value: UUID) : UUIDValue(value) {
    companion object : UUIDValueFactory<PickupId>(::PickupId)
}
