package hyperpyramid.adapter

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import hyperpyramid.ShopApiSettings.NOTIFICATION_EMAIL_SENDER
import hyperpyramid.dto.Email
import hyperpyramid.dto.OrderId
import hyperpyramid.port.Notifications
import org.http4k.config.Environment
import org.http4k.connect.amazon.ses.Http
import org.http4k.connect.amazon.ses.SES
import org.http4k.connect.amazon.ses.model.Body
import org.http4k.connect.amazon.ses.model.Content
import org.http4k.connect.amazon.ses.model.Destination
import org.http4k.connect.amazon.ses.model.EmailAddress
import org.http4k.connect.amazon.ses.model.EmailContent
import org.http4k.connect.amazon.ses.model.Message
import org.http4k.connect.amazon.ses.sendEmail
import org.http4k.core.HttpHandler

fun SESNotifications(env: Environment, outgoingHttp: HttpHandler) = object : Notifications {
    private val ses = SES.Http(env, outgoingHttp)

    override fun collectOrder(user: Email, orderId: OrderId): Result4k<Unit, Exception> =
        ses.sendEmail(
            fromEmailAddress = EmailAddress.of(env[NOTIFICATION_EMAIL_SENDER].value),
            destination = Destination(
                toAddresses = setOf(EmailAddress.of(user.value))
            ),
            content = EmailContent(
                simple = Message(
                    subject = Content("Your order $orderId"),
                    body = Body(
                        text = Content("Please collect your order using the code: $orderId", Charsets.UTF_8),
                    )
                )
            )
        )
            .map { }
            .mapFailure { Exception(it.message) }
}
