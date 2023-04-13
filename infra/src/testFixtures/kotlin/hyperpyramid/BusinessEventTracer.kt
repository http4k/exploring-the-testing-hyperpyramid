package hyperpyramid

import hyperpyramid.app.BusinessEvent
import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType.Queue
import org.http4k.tracing.FireAndForget
import org.http4k.tracing.Tracer

fun BusinessEventTracer(actorResolver: ActorResolver) = Tracer { parent, _, _ ->
    parent
        .takeIf { it.event is BusinessEvent }
        ?.let {
            FireAndForget(
                actorResolver(it),
                Actor("event-stream", Queue),
                (it.event as BusinessEvent).toString(),
                emptyList()
            )
        }
        ?.let { listOf(it) }
        ?: emptyList()
}
