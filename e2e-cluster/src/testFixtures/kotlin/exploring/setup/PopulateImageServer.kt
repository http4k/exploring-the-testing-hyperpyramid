package exploring.setup

import exploring.ImageSettings
import exploring.TheInternet
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketKey
import org.http4k.connect.amazon.s3.putObject

fun PopulateImageServer(theInternet: TheInternet): CloudSetup = {
    it.also {
        with(theInternet.s3) {
            s3Client().createBucket(ImageSettings.IMAGE_BUCKET(it), AWS_REGION(it))
            s3BucketClient(ImageSettings.IMAGE_BUCKET(it), AWS_REGION(it)).apply {
                putObject(BucketKey.of("1"), "1".byteInputStream(), emptyList())
                putObject(BucketKey.of("2"), "2".byteInputStream(), emptyList())
                putObject(BucketKey.of("3"), "3".byteInputStream(), emptyList())
                putObject(BucketKey.of("4"), "4".byteInputStream(), emptyList())
            }
        }
    }
}
