package hyperpyramid.http

import org.http4k.client.JavaHttpClient
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom
import org.http4k.routing.reverseProxy

fun ProxyCallToLiveServerFor(services: ServiceDiscovery) = reverseProxy(
    *listOf("api-gateway", "cognito", "dept-store", "email", "s3")
        .flatMap {
            val http = SetBaseUriFrom(services(it)).then(JavaHttpClient())
            listOf(services(it).authority to http, it to http)
        }
        .toTypedArray()
)
