package hyperpyramid.port

import dev.forkhandles.result4k.mapFailure
import hyperpyramid.dto.ItemId
import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.getObject
import org.http4k.connect.amazon.s3.model.BucketKey

class ImagesHub(private val s3: S3Bucket) {
    fun image(id: ItemId) =
        s3.getObject(BucketKey.of(id.value))
            .mapFailure { Exception(it.message) }
}
