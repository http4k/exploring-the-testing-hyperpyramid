package exploring.app

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.events.HttpEvent.Outgoing
import org.http4k.filter.ClientFilters.RequestTracing
import org.http4k.filter.ResponseFilters.ReportHttpTransaction

fun AppOutgoingHttp(debug: Boolean, base: HttpHandler, events: Events) =
    Debug(debug)
        .then(RequestTracing())
    .then(ReportHttpTransaction { events(Outgoing(it)) })
    .then(base)
