description = "Infra"

dependencies {
    api(platform(Http4k.bom))
    api(platform(libs.http4k.connect.bom))
    api(platform(libs.forkhandles.bom))

    api(Http4k.cloudnative)
    api(Http4k.core)
    api(Http4k.format.moshi)
    api(libs.values4k)

    testFixturesApi(Testing.junit.jupiter.api)
    testFixturesApi(Testing.junit.jupiter.engine)

    testFixturesApi(Http4k.incubator)
    testFixturesApi(Http4k.testing.strikt)
    testFixturesApi(libs.http4k.connect.amazon.sns.fake)
}
