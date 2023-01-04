dependencies {
    rootProject.subprojects
        .filter { it.name != project.name }
        .forEach {
            testFixturesApi(testFixtures(project(":" + it.name)))
            testFixturesApi(project(":" + it.name))
        }
}
