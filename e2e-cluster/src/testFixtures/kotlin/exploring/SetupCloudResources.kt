package exploring

import exploring.WarehouseSettings.DISPATCH_QUEUE
import exploring.WebsiteSettings.NOTIFICATION_TOPIC_ARN
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.sns.Http
import org.http4k.connect.amazon.sns.SNS
import org.http4k.connect.amazon.sns.createTopic
import org.http4k.connect.amazon.sns.model.TopicName
import org.http4k.connect.amazon.sqs.Http
import org.http4k.connect.amazon.sqs.SQS
import org.http4k.connect.amazon.sqs.createQueue
import org.http4k.connect.amazon.sqs.model.QueueName
import org.http4k.core.HttpHandler
import org.http4k.core.Uri

fun setupCloudResources(env: Environment, theInternet: HttpHandler) {
    SQS.Http(env, theInternet).createQueue(
        QueueName.parse(DISPATCH_QUEUE(env)),
        emptyList(),
        emptyMap()
    )

    SNS.Http(env, theInternet)
        .createTopic(NOTIFICATION_TOPIC_ARN(env).resourceId(TopicName::of), emptyList(), emptyMap())
}

private fun QueueName.Companion.parse(uri: Uri) = QueueName.of(uri.path.substringAfterLast('/'))
