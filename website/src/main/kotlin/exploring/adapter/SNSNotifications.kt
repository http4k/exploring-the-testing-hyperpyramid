package exploring.adapter

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import exploring.port.Notifications
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.sns.Http
import org.http4k.connect.amazon.sns.SNS
import org.http4k.connect.amazon.sns.model.PhoneNumber
import org.http4k.connect.amazon.sns.publishMessage
import org.http4k.core.HttpHandler
import java.util.UUID

fun Notifications.Companion.SNS(env: Environment, outgoingHttp: HttpHandler) = object : Notifications {
    private val sns = SNS.Http(env, outgoingHttp)

    override fun collectOrder(user: PhoneNumber, trackingId: UUID): Result4k<UUID, Exception> =
        sns.publishMessage(
            "Please collect your order $trackingId",
            phoneNumber = user,
            topicArn = NOTIFICATION_TOPIC_ARN(env))
            .map { UUID.fromString(it.MessageId.value) }
            .mapFailure { Exception(it.message) }
}
