dependencies {
    api(project(":infra"))

    api(Http4k.securityOauth)

    testFixturesApi(testFixtures(project(":infra")))
    testFixturesApi(libs.http4k.connect.amazon.cognito.fake)
}
