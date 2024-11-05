import extension.libs
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * This plugin will configure compiler modules.
 * - update version to kotlin-aware one (ex: 2.0.0-0.1.0)
 * - configure version-specific srcDirs (ex: v2.0.0)
 */
plugins {
    id("org.jetbrains.kotlin.jvm")
}

val kotlinVersion = libs.findVersion("kotlin").get()
val kotlinAwareVersion = "$kotlinVersion-$version"
version = kotlinAwareVersion

configure<KotlinJvmProjectExtension> {
    sourceSets.forEach { sourceSet ->
        val srcDirs = sourceSet.kotlin.srcDirs
        val sourceSetRootPath = srcDirs.first().toPath().parent

        val kotlinVersionSourceDirName = "v${kotlinVersion.toString().replace(".", "_")}"
        val versionSpecificSourceDir = sourceSetRootPath.resolve(kotlinVersionSourceDirName).toFile()

        sourceSet.kotlin.setSrcDirs(srcDirs + versionSpecificSourceDir)
    }
}
