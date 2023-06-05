package hyperpyramid.adapter

import hyperpyramid.port.EventStream
import org.http4k.events.Events
import org.http4k.filter.ZipkinTracesStorage
import org.http4k.filter.inChildSpan

fun EventsBasedEventStream(events: Events) = EventStream { businessEvent ->
    ZipkinTracesStorage.THREAD_LOCAL.inChildSpan {
        events(businessEvent)
    }
}
