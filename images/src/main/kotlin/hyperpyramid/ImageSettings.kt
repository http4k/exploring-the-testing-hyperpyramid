package hyperpyramid

import org.http4k.config.EnvironmentKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.lens.of
import org.http4k.lens.value

object ImageSettings : Settings() {
    val IMAGE_BUCKET by EnvironmentKey.value(BucketName).of().required()
}
