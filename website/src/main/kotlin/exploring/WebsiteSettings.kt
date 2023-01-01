package exploring

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.connect.amazon.core.model.ARN
import org.http4k.lens.of
import org.http4k.lens.value

object WebsiteSettings : Settings() {
    val NOTIFICATION_TOPIC_ARN by EnvironmentKey.value(ARN).of().required()
}
