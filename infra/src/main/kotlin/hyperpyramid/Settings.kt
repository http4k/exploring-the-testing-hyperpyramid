package hyperpyramid

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.boolean
import org.http4k.lens.of

abstract class Settings {
    val DEBUG by EnvironmentKey.boolean().of().defaulted(false)
}

