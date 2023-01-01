description = "Infra"

dependencies {
    api(platform(Http4k.bom))
    api(platform(libs.http4k.connect.bom))

    api(Http4k.cloudnative)
    api(Http4k.core)
    api(Http4k.format.moshi)

    testApi(Testing.junit.jupiter.api)
    testApi(Testing.junit.jupiter.engine)

    testFixturesApi(Http4k.incubator)
    testFixturesApi(libs.http4k.connect.amazon.s3.fake)
    testFixturesApi(libs.http4k.connect.amazon.dynamodb.fake)
    testFixturesApi(libs.http4k.connect.amazon.sqs.fake)
    testFixturesApi(libs.http4k.connect.amazon.sns.fake)
}
