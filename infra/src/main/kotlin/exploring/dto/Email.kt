package exploring.dto

import dev.forkhandles.values.NonBlankStringValueFactory
import dev.forkhandles.values.StringValue

class Email private constructor(value: String) : StringValue(value) {
    companion object : NonBlankStringValueFactory<Email>(::Email)
}
