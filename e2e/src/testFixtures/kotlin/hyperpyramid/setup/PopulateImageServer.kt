package hyperpyramid.setup

import hyperpyramid.ImageSettings.IMAGE_BUCKET
import hyperpyramid.TheInternet
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.s3.createBucket
import org.http4k.connect.amazon.s3.model.BucketName
import org.http4k.core.with

internal fun PopulateImageServer(theInternet: TheInternet): CloudInfraSetup = {
    with(theInternet.s3) {
        val bucketName = BucketName.of("image-cache")
        s3Client().createBucket(bucketName, AWS_REGION(it))
        s3BucketClient(bucketName, AWS_REGION(it)).PopulateBucketWithImages()
        it.with(IMAGE_BUCKET of bucketName)
    }
}

