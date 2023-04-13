dependencies {
    api(project(":infra"))
    implementation(libs.http4k.connect.amazon.ses)

    testFixturesApi(testFixtures(project(":infra")))

    testFixturesApi(libs.http4k.connect.amazon.ses.fake)
}
