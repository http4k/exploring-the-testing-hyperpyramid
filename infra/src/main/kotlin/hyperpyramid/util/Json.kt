package hyperpyramid.util

import com.fasterxml.jackson.module.kotlin.KotlinModule.Builder
import hyperpyramid.dto.Email
import hyperpyramid.dto.ItemId
import hyperpyramid.dto.OrderId
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.value
import org.http4k.format.withStandardMappings

object Json : ConfigurableJackson(
    Builder().build()
        .asConfigurable()
        .withStandardMappings()
        .value(Email)
        .value(ItemId)
        .value(OrderId)
        .done()

)
