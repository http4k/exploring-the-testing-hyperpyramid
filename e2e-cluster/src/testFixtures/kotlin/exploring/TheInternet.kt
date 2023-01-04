package exploring

import org.http4k.connect.amazon.cognito.FakeCognito
import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.ses.EmailMessage
import org.http4k.connect.amazon.ses.FakeSES
import org.http4k.connect.storage.InMemory
import org.http4k.connect.storage.Storage
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting

class TheInternet : RoutingHttpHandler {

    private val emails = Storage.InMemory<List<EmailMessage>>()

    val ses = FakeSES(emails)
    val s3 = FakeS3()
    val cognito = FakeCognito()
    val departmentStore = FakeDepartmentStore()

    val emailInbox = SESEmails(emails)

    private val http = reverseProxyRouting(
        "cognito" to cognito,
        "dept-store" to departmentStore,
        "email" to ses,
        "s3" to s3
    )

    override fun invoke(p1: Request) = http(p1)
    override fun match(request: Request) = http.match(request)
    override fun withBasePath(new: String) = http.withBasePath(new)
    override fun withFilter(new: Filter) = http.withFilter(new)
}

