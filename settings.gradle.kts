pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions").version("0.50.2")
}

include("api-gateway")
include("e2e")
include("images")
include("infra")
include("warehouse")
include("shop")
