package hyperpyramid.endpoint

import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import hyperpyramid.dto.ItemId
import hyperpyramid.port.ImagesHub
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Path
import org.http4k.lens.value
import org.http4k.routing.bind

fun GetImage(imagesHub: ImagesHub) = "/img/{id}" bind GET to {
    imagesHub.image(itemId(it))
        .map {
            when (it) {
                null -> Response(NOT_FOUND)
                else -> Response(OK).body(it)
            }
        }
        .orThrow()
}

private val itemId = Path.value(ItemId).of("id")

