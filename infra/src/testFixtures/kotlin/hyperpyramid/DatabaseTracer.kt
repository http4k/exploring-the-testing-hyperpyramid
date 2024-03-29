package hyperpyramid

import hyperpyramid.app.DbCall
import org.http4k.tracing.Actor
import org.http4k.tracing.ActorResolver
import org.http4k.tracing.ActorType.Database
import org.http4k.tracing.BiDirectional
import org.http4k.tracing.Tracer

fun DbTracer(actorResolver: ActorResolver) = Tracer { node, _ ->
    node.event
        .takeIf { it.event is DbCall }
        ?.let {
            BiDirectional(
                actorResolver(it),
                Actor("db", Database),
                (it.event as DbCall).name,
                emptyList()
            )
        }
        ?.let { listOf(it) }
        ?: emptyList()
}
