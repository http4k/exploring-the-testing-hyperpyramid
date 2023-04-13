package hyperpyramid.setup

import hyperpyramid.TheInternet
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.core.with

internal fun SetRegion(theInternet: TheInternet): CloudInfraSetup = {
    it.with(AWS_REGION of EU_WEST_1)
}
