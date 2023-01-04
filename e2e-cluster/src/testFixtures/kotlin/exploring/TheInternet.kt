package exploring

import org.http4k.connect.amazon.cognito.FakeCognito
import org.http4k.connect.amazon.ses.FakeSES
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.reverseProxyRouting

class TheInternet : RoutingHttpHandler {

    val ses = FakeSES()
    val cognito = FakeCognito()
    val departmentStore = FakeDepartmentStore()

    private val http = reverseProxyRouting(
        "email" to ses,
        "cognito" to Filter { next ->
            {
                next(it.body(it.bodyString().replace("Email", "email")))
            }
        }.then(cognito),
        "dept-store" to departmentStore
    )

    override fun invoke(p1: Request) = http(p1)
    override fun match(request: Request) = http.match(request)
    override fun withBasePath(new: String) = http.withBasePath(new)
    override fun withFilter(new: Filter) = http.withFilter(new)
}

