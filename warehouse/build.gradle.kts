description = "App2"

dependencies {
    api(project(":infra"))
    api(libs.http4k.connect.amazon.dynamodb)

    testFixturesApi(testFixtures(project(":infra")))
}
