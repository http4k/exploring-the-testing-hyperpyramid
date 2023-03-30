dependencies {
    testApi(project(":e2e"))
    implementation(Http4k.template.handlebars)

    testFixturesApi(testFixtures(project(":e2e")))
}
