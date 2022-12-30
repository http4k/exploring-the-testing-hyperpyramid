package exploring

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind

fun Login() = "/v1/login" bind { req: Request -> Response(Status.OK) }
