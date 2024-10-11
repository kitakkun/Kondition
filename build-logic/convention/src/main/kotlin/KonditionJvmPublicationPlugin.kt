@file:Suppress("UNUSED")

import extension.configureMavenPublishing
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class KonditionJvmPublicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.create<KonditionPublicationExtension>("konditionPublication")

            with(pluginManager) {
                apply("com.vanniktech.maven.publish")
            }

            afterEvaluate {
                val extension = extensions.getByType(KonditionPublicationExtension::class.java)
                val artifactId = extension.artifactId
                if (artifactId.isBlank()) error("Artifact ID must be specified.")
                configureMavenPublishing(artifactId)
            }
        }
    }
}
