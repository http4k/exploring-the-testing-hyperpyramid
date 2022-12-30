package exploring

import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind

fun Endpoint1(http: HttpHandler) =
    "/v1/app1" bind POST to { req: Request ->
        Response(OK)
    }
