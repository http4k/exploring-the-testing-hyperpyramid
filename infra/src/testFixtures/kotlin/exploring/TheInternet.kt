package exploring

import org.http4k.connect.amazon.sns.FakeSNS
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting

class TheInternet : RoutingHttpHandler {

    val sns = FakeSNS()
    val departmentStore = FakeDepartmentStore()

    private val http = reverseProxyRouting(
        "sns" to sns,
        "dept-store" to departmentStore
    )

    override fun invoke(p1: Request) = http(p1)
    override fun match(request: Request) = http.match(request)
    override fun withBasePath(new: String) = http.withBasePath(new)
    override fun withFilter(new: Filter) = http.withFilter(new)
}

