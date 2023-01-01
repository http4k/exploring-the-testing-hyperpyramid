package exploring.app

import org.http4k.core.then
import org.http4k.events.Events
import org.http4k.events.HttpEvent.Incoming
import org.http4k.filter.ResponseFilters.ReportHttpTransaction
import org.http4k.filter.ServerFilters.RequestTracing
import org.http4k.routing.RoutingHttpHandler

fun AppIncomingHttp(debug: Boolean, events: Events, base: RoutingHttpHandler) =
    Debug(debug)
        .then(HandleError(events))
        .then(RequestTracing())
        .then(ReportHttpTransaction { events(Incoming(it)) })
        .then(base)

