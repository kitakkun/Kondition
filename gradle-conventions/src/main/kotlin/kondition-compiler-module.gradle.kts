import extension.libs

/**
 * This plugin will configure compiler modules.
 * - update version to kotlin-aware one (ex: 2.0.0-0.1.0)
 */
plugins {
    id("org.jetbrains.kotlin.jvm")
}

val kotlinVersion = libs.findVersion("kotlin").get()
val kotlinAwareVersion = "$kotlinVersion-$version"
version = kotlinAwareVersion
