package exploring.adapter

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import exploring.WarehouseSettings.DISPATCH_QUEUE
import exploring.dto.DispatchRequest
import exploring.port.Dispatcher
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.sqs.Http
import org.http4k.connect.amazon.sqs.SQS
import org.http4k.connect.amazon.sqs.sendMessage
import org.http4k.core.HttpHandler
import org.http4k.format.Moshi.asFormatString
import java.util.UUID

fun Dispatcher.Companion.SQS(env: Environment, http: HttpHandler) =
    object : Dispatcher {
        private val sqs = SQS.Http(env, http)
        override fun dispatch(dispatchRequest: DispatchRequest) =
            sqs.sendMessage(DISPATCH_QUEUE(env), asFormatString(dispatchRequest))
                .map { UUID.fromString(it.MessageId.value.substringAfter('/')) } // todo fix this when upgraded connect
                .mapFailure { Exception(it.message) }
    }

