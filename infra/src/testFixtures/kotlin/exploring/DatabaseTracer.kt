package exploring

import exploring.app.DbCall
import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType
import org.http4k.tracing.BiDirectional
import org.http4k.tracing.Tracer

fun DbTracer(actorResolver: ActorResolver) = Tracer { parent, _, _ ->
    parent
        .takeIf { it.event is DbCall }
        ?.let {
            BiDirectional(
                actorResolver(it),
                Actor("db", ActorType.Database),
                (it.event as DbCall).name,
                emptyList()
            )
        }
        ?.let { listOf(it) }
        ?: emptyList()
}
