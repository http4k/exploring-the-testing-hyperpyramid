dependencies {
    api(project(":infra"))
    api(libs.http4k.connect.storage.jdbc)
    api(libs.hikaricp)
    api(libs.h2)
    api(JetBrains.exposed.core)
    api(JetBrains.exposed.jdbc)

    testFixturesApi(testFixtures(project(":infra")))
    testFixturesApi(Http4k.testing.chaos)}
