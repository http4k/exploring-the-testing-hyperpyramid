package hyperpyramid

import org.http4k.connect.amazon.s3.FakeS3
import org.http4k.connect.amazon.ses.EmailMessage
import org.http4k.connect.amazon.ses.FakeSES
import org.http4k.connect.storage.InMemory
import org.http4k.connect.storage.Storage
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.routing.reverseProxy

class TheInternet: HttpHandler {
    val emails = Storage.InMemory<List<EmailMessage>>()

    val departmentStore = FakeDepartmentStore()
    val ses = FakeSES(emails)
    val s3 = FakeS3()

    val http = reverseProxy(
        "dept-store" to departmentStore,
        "email" to ses,
        "s3" to s3
    )

    override fun invoke(request: Request) = http(request)
}


