package exploring

import exploring.app.AppEvents
import exploring.app.AppOutgoingHttp
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.filter.ClientFilters.ResetRequestTracing
import java.time.Clock

abstract class Actor(name: String, http: HttpHandler, events: Events) {
    val http = ResetRequestTracing()
        .then(AppOutgoingHttp(false, AppEvents(name, Clock.systemUTC(), events), http))
}


