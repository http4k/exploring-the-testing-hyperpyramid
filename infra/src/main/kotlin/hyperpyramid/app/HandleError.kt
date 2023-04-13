package hyperpyramid.app

import org.http4k.core.Response
import org.http4k.core.Status.Companion.SERVICE_UNAVAILABLE
import org.http4k.events.Event.Companion.Error
import org.http4k.events.Events
import org.http4k.filter.ServerFilters.CatchAll

fun HandleError(events: Events) = CatchAll {
    events(Error("uncaught!", it))
    Response(SERVICE_UNAVAILABLE)
}
