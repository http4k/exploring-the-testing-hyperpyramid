package hyperpyramid.setup

import org.http4k.connect.amazon.s3.S3Bucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.putObject

fun S3Bucket.PopulateBucketWithImages() {
    putObject(BucketKey.of("1"), javaClass.getResourceAsStream("/banana.jpg"), emptyList())
    putObject(BucketKey.of("2"), javaClass.getResourceAsStream("/bottom.jpg"), emptyList())
    putObject(BucketKey.of("3"), javaClass.getResourceAsStream("/minions.jpg"), emptyList())
    putObject(BucketKey.of("4"), javaClass.getResourceAsStream("/guitar.jpg"), emptyList())
}
