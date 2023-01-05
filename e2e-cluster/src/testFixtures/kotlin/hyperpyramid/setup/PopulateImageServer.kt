package hyperpyramid.setup

import hyperpyramid.ImageSettings.IMAGE_BUCKET
import hyperpyramid.TheInternet
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.connect.amazon.s3.putObject
import org.http4k.core.with

internal fun PopulateImageServer(theInternet: TheInternet): CloudSetup = {
    with(theInternet.s3) {
        val bucketName = BucketName.of("image-cache")
        s3Client().createBucket(bucketName, AWS_REGION(it))
        s3BucketClient(bucketName, AWS_REGION(it)).apply {
            putObject(BucketKey.of("1"), "1".byteInputStream(), emptyList())
            putObject(BucketKey.of("2"), "2".byteInputStream(), emptyList())
            putObject(BucketKey.of("3"), "3".byteInputStream(), emptyList())
            putObject(BucketKey.of("4"), "4".byteInputStream(), emptyList())
        }
        it.with(IMAGE_BUCKET of bucketName)
    }
}
