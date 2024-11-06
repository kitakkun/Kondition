plugins {
    id("com.diffplug.spotless")
}

spotless {
    isEnforceCheck = false
    kotlin {
        target("**/*.kt")
        ktlint("1.3.1")
            .setEditorConfigPath("${rootDir.path}/.editorconfig")
    }
}
