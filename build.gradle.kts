import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.gradle.test.logger.plugin)
    }
}

plugins {
    `java-test-fixtures`
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "java-test-fixtures")

    repositories {
        mavenCentral()
    }

    dependencies {
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
