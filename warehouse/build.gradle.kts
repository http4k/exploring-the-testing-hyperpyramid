description = "App2"

dependencies {
    api(project(":infra"))
    api(libs.http4k.connect.amazon.sqs)
    api(libs.http4k.connect.storage.jdbc)
    api(libs.hikaricp)
    api(libs.h2)

    testFixturesApi(testFixtures(project(":infra")))
}
