description = "App2"

dependencies {
    api(project(":infra"))
    api(libs.http4k.connect.amazon.dynamodb)
    api(libs.http4k.connect.amazon.sqs)

    testFixturesApi(testFixtures(project(":infra")))
}
