package exploring.setup

import org.http4k.cloudnative.env.Environment

typealias CloudSetup = (Environment) -> Environment

operator fun Iterable<CloudSetup>.invoke(env: Environment) = fold(env) { acc, updateCloud -> updateCloud(acc) }
