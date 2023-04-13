dependencies {
    api(project(":infra"))
    api(libs.http4k.connect.amazon.s3)

    testFixturesApi(testFixtures(project(":infra")))
    testFixturesApi(libs.http4k.connect.amazon.s3.fake)
}
