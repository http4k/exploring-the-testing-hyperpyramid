description = "App2"

dependencies {
    api(project(":infra"))
    implementation(Http4k.template.handlebars)
    api(libs.http4k.connect.amazon.sns)

    testFixturesApi(Http4k.testing.webdriver)
    testFixturesApi(testFixtures(project(":infra")))
}
