package exploring

import exploring.actors.Emails
import exploring.dto.Email
import org.http4k.connect.amazon.ses.EmailMessage
import org.http4k.connect.amazon.ses.model.EmailAddress
import org.http4k.connect.storage.Storage

fun SESEmails(emails: Storage<List<EmailMessage>>) = Emails { email ->
    emails.keySet()
        .flatMap { emails[it]!!.filter { it.to.contains(EmailAddress.of(email.value)) } }
        .map { Email.of(it.source.value) to ((it.message.html ?: it.message.text)?.value ?: error("!")) }
}
