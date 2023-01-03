package exploring.dto

import dev.forkhandles.values.NonBlankStringValueFactory
import dev.forkhandles.values.StringValue

class Phone private constructor(value: String) : StringValue(value) {
    companion object : NonBlankStringValueFactory<Phone>(::Phone)
}
