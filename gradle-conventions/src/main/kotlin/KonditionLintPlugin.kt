import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KonditionLintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")

            configure<SpotlessExtension> {
                isEnforceCheck = false
                kotlin {
                    target("**/*.kt")
                    ktlint("1.3.1")
                        .setEditorConfigPath("${target.rootDir.path}/.editorconfig")
                }
            }
        }
    }
}
