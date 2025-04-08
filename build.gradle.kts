import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    idea
    `java-test-fixtures`
}

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
repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "idea")
    apply(plugin = "java-test-fixtures")

    repositories {
        mavenCentral()
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "21"
            }
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
