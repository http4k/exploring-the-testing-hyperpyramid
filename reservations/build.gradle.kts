description = "App1"

dependencies {
    api(project(":infra"))
    api(libs.http4k.connect.amazon.sqs)

    testFixturesApi(testFixtures(project(":infra")))
}
