package hyperpyramid.endpoint

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.routing.asRouter
import org.http4k.routing.bind
import org.http4k.security.OAuthProvider

fun ForwardOntoWebsite(oAuthProvider: OAuthProvider, http: HttpHandler) =
    { _: Request -> true }.asRouter() bind
        oAuthProvider.authFilter
            .then(SetHostFrom(Uri.of("http://website")))
            .then(http)
