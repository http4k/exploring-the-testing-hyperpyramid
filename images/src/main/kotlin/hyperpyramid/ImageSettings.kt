package hyperpyramid

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.lens.of
import org.http4k.lens.value

object ImageSettings : Settings() {
    val IMAGE_BUCKET by EnvironmentKey.value(BucketName).of().required()
}
