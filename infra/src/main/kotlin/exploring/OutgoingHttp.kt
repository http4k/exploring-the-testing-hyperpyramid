package exploring

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.events.HttpEvent.Outgoing
import org.http4k.filter.ClientFilters.RequestTracing
import org.http4k.filter.ResponseFilters.ReportHttpTransaction

fun OutgoingHttp(base: HttpHandler, events: Events) = RequestTracing()
    .then(ReportHttpTransaction { events(Outgoing(it)) })
    .then(base)
