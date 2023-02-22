plugins {
    id("de.fayard.refreshVersions").version("0.50.2")
}

include("api-gateway")
include("e2e-cluster")
include("images")
include("infra")
include("warehouse")
include("website")

include("presentation")
project(":presentation").projectDir = File(".presentation")

