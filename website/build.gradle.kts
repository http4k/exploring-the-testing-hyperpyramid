description = "App2"

dependencies {
    api(project(":infra"))
    implementation(Http4k.template.handlebars)
    implementation(libs.http4k.connect.amazon.ses)

    testFixturesApi(testFixtures(project(":infra")))

    testFixturesApi(Http4k.testing.webdriver)
    testFixturesApi(libs.http4k.connect.amazon.ses.fake)
}
