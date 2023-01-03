package exploring.util

import com.squareup.moshi.Moshi
import exploring.dto.ItemId
import exploring.dto.Phone
import exploring.dto.PickupId
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
        .value(Phone)
        .value(PickupId)
        .done()
)
