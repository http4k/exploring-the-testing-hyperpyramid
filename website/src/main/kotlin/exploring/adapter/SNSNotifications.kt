package exploring.adapter

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import exploring.dto.Phone
import exploring.dto.PickupId
import exploring.port.Notifications
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.sns.Http
import org.http4k.connect.amazon.sns.SNS
import org.http4k.connect.amazon.sns.model.PhoneNumber
import org.http4k.connect.amazon.sns.publishMessage
import org.http4k.core.HttpHandler

fun Notifications.Companion.SNS(env: Environment, outgoingHttp: HttpHandler) = object : Notifications {
    private val sns = SNS.Http(env, outgoingHttp)

    override fun collectOrder(phone: Phone, pickupId: PickupId): Result4k<Unit, Exception> =
        sns.publishMessage(
            "Please collect your order using the code: $pickupId",
            phoneNumber = PhoneNumber.of(phone.value),
            topicArn = NOTIFICATION_TOPIC_ARN(env)
        )
            .map { }
            .mapFailure { Exception(it.message) }
}
