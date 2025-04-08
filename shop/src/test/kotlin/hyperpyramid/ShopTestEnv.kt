    package hyperpyramid

import hyperpyramid.ShopApiSettings.NOTIFICATION_EMAIL_SENDER
import hyperpyramid.ShopApiSettings.WAREHOUSE_URL
import hyperpyramid.dto.Email
import org.http4k.config.Environment.Companion.defaults
import org.http4k.connect.amazon.AWS_ACCESS_KEY_ID
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.AWS_SECRET_ACCESS_KEY
import org.http4k.connect.amazon.core.model.AccessKeyId
import org.http4k.connect.amazon.core.model.Region.Companion.EU_WEST_1
import org.http4k.connect.amazon.core.model.SecretAccessKey
import org.http4k.core.Uri

val ShopTestEnv = defaults(
    AWS_REGION of EU_WEST_1,
    AWS_ACCESS_KEY_ID of AccessKeyId.of("access-key-id"),
    AWS_SECRET_ACCESS_KEY of SecretAccessKey.of("secret-access-key"),
    NOTIFICATION_EMAIL_SENDER of Email.of("orders@http4k.org"),
    WAREHOUSE_URL of Uri.of("http://warehouse")
)
