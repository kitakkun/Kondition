rootProject.name = "build-logic"

pluginManagement {
    includeBuild("../gradle-conventions-settings")
}

plugins {
    id("settings-conventions")
}

include(":convention")
