package exploring.app

import org.http4k.events.EventFilters.AddServiceName
import org.http4k.events.EventFilters.AddTimestamp
import org.http4k.events.EventFilters.AddZipkinTraces
import org.http4k.events.Events
import org.http4k.events.then
import java.time.Clock

fun AppEvents(name: String, clock: Clock, base: Events) = AddZipkinTraces()
    .then(AddServiceName(name))
    .then(AddTimestamp(clock))
    .then(base)
