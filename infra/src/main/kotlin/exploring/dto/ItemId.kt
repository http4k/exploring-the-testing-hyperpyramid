package exploring.dto

import dev.forkhandles.values.StringValue
import dev.forkhandles.values.StringValueFactory

class ItemId private constructor(value: String) : StringValue(value) {
    companion object : StringValueFactory<ItemId>(::ItemId)
}
