package exploring.adapter

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import exploring.WarehouseSettings.DISPATCH_QUEUE
import exploring.dto.ItemPickup
import exploring.dto.PickupId
import exploring.port.Dispatcher
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.sqs.Http
import org.http4k.connect.amazon.sqs.SQS
import org.http4k.connect.amazon.sqs.sendMessage
import org.http4k.core.HttpHandler
import org.http4k.format.Moshi.asFormatString

fun Dispatcher.Companion.SQS(env: Environment, http: HttpHandler) =
    object : Dispatcher {
        private val sqs = SQS.Http(env, http)
        override fun dispatch(itemPickup: ItemPickup) =
            sqs.sendMessage(DISPATCH_QUEUE(env), asFormatString(itemPickup))
                .map { PickupId.parse(it.MessageId.value.substringAfter('/')) } // todo fix this when upgraded connect
                .mapFailure { Exception(it.message) }
    }

