package hyperpyramid.adapter

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import hyperpyramid.ShopApiSettings.NOTIFICATION_EMAIL_SENDER
import hyperpyramid.dto.Email
import hyperpyramid.dto.OrderId
import hyperpyramid.port.Notifications
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.ses.Http
import org.http4k.connect.amazon.ses.SES
import org.http4k.connect.amazon.ses.action.SendEmail
import org.http4k.connect.amazon.ses.model.Body
import org.http4k.connect.amazon.ses.model.Destination
import org.http4k.connect.amazon.ses.model.EmailAddress
import org.http4k.connect.amazon.ses.model.Message
import org.http4k.connect.amazon.ses.model.Subject
import org.http4k.core.HttpHandler

fun SESNotifications(env: Environment, outgoingHttp: HttpHandler) = object : Notifications {
    private val sns = SES.Http(env, outgoingHttp)

    override fun collectOrder(user: Email, orderId: OrderId): Result4k<Unit, Exception> =
        sns(
            SendEmail(
                EmailAddress.of(env[NOTIFICATION_EMAIL_SENDER].value),
                Destination(setOf(EmailAddress.of(user.value))),
                Message(
                    Subject.of("Your order $orderId"),
                    Body("Please collect your order using the code: $orderId")
                )
            )
        )
            .map { }
            .mapFailure { Exception(it.message) }
}
