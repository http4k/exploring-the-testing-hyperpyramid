package exploring

import exploring.app.AppOutgoingHttp
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.events.EventFilters.AddServiceName
import org.http4k.events.Events
import org.http4k.events.then
import org.http4k.filter.ClientFilters.ResetRequestTracing

abstract class Actor(
    name: String,
    http: HttpHandler,
    events: Events
) {
    val http = ResetRequestTracing()
        .then(AppOutgoingHttp(false, AddServiceName(name).then(events), http))
}


