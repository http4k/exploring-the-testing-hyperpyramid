package exploring

import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.sns.Http
import org.http4k.connect.amazon.sns.SNS
import org.http4k.connect.amazon.sns.createTopic
import org.http4k.connect.amazon.sns.model.TopicName
import org.http4k.core.HttpHandler

fun setupCloudResources(env: Environment, theInternet: HttpHandler) {
    SNS.Http(env, theInternet)
        .createTopic(NOTIFICATION_TOPIC_ARN(env).resourceId(TopicName::of), emptyList(), emptyMap())
}
