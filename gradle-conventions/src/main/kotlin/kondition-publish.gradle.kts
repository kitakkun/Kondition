import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType

open class KonditionPublicationExtension {
    var artifactId: String = ""
}

extensions.create<KonditionPublicationExtension>("konditionPublication")

plugins {
    id("com.vanniktech.maven.publish")
}

afterEvaluate {
    val extension = extensions.getByType(KonditionPublicationExtension::class.java)
    val artifactId = extension.artifactId
    if (artifactId.isBlank()) error("Artifact ID must be specified.")

    configure<MavenPublishBaseExtension> {
        coordinates(artifactId = artifactId)

        pom {
            name.set("Kondition")
            description.set("Kondition ensure that your Kotlin code runs under some conditions are met. It inserts code to verify conditions for value parameters or variables at compile time.")
            inceptionYear.set("2024")
            url.set("https://github.com/kitakkun/Kondition")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://github.com/kitakkun/Kondition/blob/master/LICENSE")
                    distribution.set("repo")
                }
                developers {
                    developer {
                        id.set("kitakkun")
                        name.set("kitakkun")
                        url.set("https://github.com/kitakkun")
                    }
                }
                scm {
                    url.set("https://github.com/kitakkun/Kondition")
                    connection.set("scm:git:git://github.com/kitakkun/Kondition.git")
                    developerConnection.set("scm:git:ssh://git@github.com/kitakkun/Kondition.git")
                }
            }
        }

        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()

        // avoid failure when executing publishToMavenLocal
        tasks.withType(org.gradle.plugins.signing.Sign::class).configureEach {
            onlyIf {
                !gradle.startParameter.taskNames.contains("publishToMavenLocal")
            }
        }
    }
}
