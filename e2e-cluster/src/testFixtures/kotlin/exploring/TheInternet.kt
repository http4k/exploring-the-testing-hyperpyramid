package exploring

import exploring.actors.Emails
import exploring.dto.Email
import org.http4k.connect.amazon.cognito.FakeCognito
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.ses.EmailMessage
import org.http4k.connect.amazon.ses.FakeSES
import org.http4k.connect.amazon.ses.model.EmailAddress
import org.http4k.connect.storage.InMemory
import org.http4k.connect.storage.Storage
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting

class TheInternet(services: ServiceDiscovery) : RoutingHttpHandler {

    private val emails = Storage.InMemory<List<EmailMessage>>()

    val cognito = FakeCognito()
    val departmentStore = FakeDepartmentStore()
    val ses = FakeSES(emails)
    val s3 = FakeS3()

    val emailInbox = SESEmails(emails)

    private val http = reverseProxyRouting(
        services("cognito").authority to cognito,
        services("dept-store").authority to departmentStore,
        services("email").authority to ses,
        services("s3").authority to s3
    )

    override fun invoke(p1: Request) = http(p1)
    override fun match(request: Request) = http.match(request)
    override fun withBasePath(new: String) = http.withBasePath(new)
    override fun withFilter(new: Filter) = http.withFilter(new)
}

private fun SESEmails(emails: Storage<List<EmailMessage>>) = Emails { email ->
    emails.keySet()
        .flatMap { emails[it]!!.filter { it.to.contains(EmailAddress.of(email.value)) } }
        .map { Email.of(it.source.value) to ((it.message.html ?: it.message.text)?.value ?: error("!")) }
}


