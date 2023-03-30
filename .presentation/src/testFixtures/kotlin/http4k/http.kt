package http4k

import org.http4k.core.Request
import org.http4k.core.Response

typealias HttpHandler = (Request) -> Response
