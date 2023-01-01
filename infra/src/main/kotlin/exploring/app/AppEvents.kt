package exploring.app

import org.http4k.events.EventFilters
import org.http4k.events.Events
import org.http4k.events.then
import java.time.Clock

fun AppEvents(name: String, clock: Clock, base: Events) = EventFilters.AddServiceName(name)
    .then(EventFilters.AddTimestamp(clock))
    .then(base)
