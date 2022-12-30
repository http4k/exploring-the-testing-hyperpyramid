description = "e2e cluster & tests"

dependencies {
    rootProject.subprojects
        .filter { it.name != project.name }
        .forEach { testFixturesApi(project(":" + it.name)) }

    testFixturesApi(testFixtures(project(":infra")))
}
