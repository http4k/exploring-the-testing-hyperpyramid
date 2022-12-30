description = "Platform Gateway"

dependencies {
    api(project(":infra"))

    testFixturesApi(testFixtures(project(":infra")))
}
