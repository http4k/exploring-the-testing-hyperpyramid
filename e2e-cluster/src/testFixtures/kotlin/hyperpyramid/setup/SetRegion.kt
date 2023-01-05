package hyperpyramid.setup

import hyperpyramid.TheInternet
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region
import org.http4k.core.with

internal fun SetRegion(theInternet: TheInternet): CloudSetup = {
    it.with(AWS_REGION of Region.EU_WEST_1)
}
