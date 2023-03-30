dependencies {
    rootProject.subprojects
        .filter { it.name != project.name }
        .filter { it.name != "presentation" }
        .forEach {
            testFixturesApi(testFixtures(project(":" + it.name)))
            testFixturesApi(project(":" + it.name))
        }
}
