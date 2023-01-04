package exploring.adapter

import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import exploring.dto.OrderId
import exploring.dto.Phone
import exploring.port.Notifications
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

fun Notifications.Companion.SES(env: Environment, outgoingHttp: HttpHandler) = object : Notifications {
    private val sns = SES.Http(env, outgoingHttp)

    override fun collectOrder(phone: Phone, orderId: OrderId): Result4k<Unit, Exception> =
        sns(
            SendEmail(
                EmailAddress.of("foo@exploring.com"),
                Destination(setOf(EmailAddress.of(phone.value))),
                Message(Subject.of("Your order $orderId"),
                    Body( "Please collect your order using the code: $orderId")
                )
            )
        )
            .map { }
            .mapFailure { Exception(it.message) }
}
