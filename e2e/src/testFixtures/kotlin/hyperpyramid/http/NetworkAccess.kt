package hyperpyramid.http

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_IMPLEMENTED

/**
 * Combines all HTTP routing into a single place.
 */
class NetworkAccess : HttpHandler {
    var http: HttpHandler? = null

    override fun invoke(p1: Request) =
        http?.invoke(p1) ?: Response(NOT_IMPLEMENTED).body("No mapped host for: ${p1.uri}")
}

