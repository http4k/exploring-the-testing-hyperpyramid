package hyperpyramid.dto

import dev.forkhandles.values.IntValue
import dev.forkhandles.values.IntValueFactory

class OrderId private constructor(value: Int) : IntValue(value) {
    companion object : IntValueFactory<OrderId>(::OrderId)
}
