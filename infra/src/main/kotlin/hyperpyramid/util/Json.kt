package hyperpyramid.util

import com.squareup.moshi.Moshi
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.format.ConfigurableMoshi
import org.http4k.format.EventAdapter
import org.http4k.format.ListAdapter
import org.http4k.format.MapAdapter
import org.http4k.format.ThrowableAdapter
import org.http4k.format.asConfigurable
import org.http4k.format.value
import org.http4k.format.withStandardMappings

object Json : ConfigurableMoshi(
    Moshi.Builder()
        .addLast(EventAdapter)
        .addLast(ThrowableAdapter)
        .addLast(ListAdapter)
        .addLast(MapAdapter)
        .asConfigurable()
        .withStandardMappings()
        .value(ItemId)
        .value(Email)
        .value(OrderId)
        .done()
)
