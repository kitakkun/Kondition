package io.github.kitakkun.kondition.compiler.fir.checker

import io.github.kitakkun.kondition.compiler.fir.KonditionFirExtensionRegistrar
import io.mockk.spyk
import io.mockk.unmockkAll
import org.jetbrains.kotlin.KtVirtualFileSourceFile
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.GroupedKtSources
import org.jetbrains.kotlin.cli.common.config.addKotlinSourceRoots
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.VfsBasedProjectEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.pipeline.ModuleCompilerInput
import org.jetbrains.kotlin.cli.jvm.compiler.pipeline.compileModuleToAnalyzedFir
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFileManager
import org.jetbrains.kotlin.com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.diagnostics.DiagnosticReporterFactory
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.metadata.jvm.deserialization.JvmProtoBufUtil
import org.jetbrains.kotlin.modules.TargetId
import org.jetbrains.kotlin.platform.CommonPlatforms
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test

class NonApplicableAnnotationUsageCheckerTest {
    @Test
    fun test() {
        val disposable = Disposer.newDisposable()
        val messageCollector = object : MessageCollector {
            override fun clear() = Unit
            override fun hasErrors() = false

            override fun report(
                severity: CompilerMessageSeverity,
                message: String,
                location: CompilerMessageSourceLocation?,
            ) {
                val destination = when (severity) {
                    CompilerMessageSeverity.LOGGING -> null
                    CompilerMessageSeverity.EXCEPTION, CompilerMessageSeverity.ERROR -> System.err
                    else -> System.out
                }
                destination?.println(message)
            }
        }

        val tempPath = Files.createTempFile("inmemory", ".kt")
        tempPath.toFile().writeText(
            """
                import io.github.kitakkun.kondition.core.annotation.NonEmpty
                
                fun test(@NonEmpty numberValue: Int) {
                }
            """.trimIndent()
        )
        val sources: Collection<File> = listOf(
            tempPath.toFile()
        )
        val classpath: Collection<File> = listOf(
            File("/Users/kitakkun/Documents/GitHub/kotlin/kondition/compiler/build/libs/compiler.jar"),
            File("/Users/kitakkun/Documents/GitHub/kotlin/kondition/core/build/libs/core-jvm.jar")
        )
        val targetName = "unnamed"
        val configuration = CompilerConfiguration()
        configuration.put(CommonConfigurationKeys.MODULE_NAME, targetName)
        configuration.put(CommonConfigurationKeys.USE_FIR, true)
        configuration.put(CommonConfigurationKeys.USE_FIR_EXTENDED_CHECKERS, true)
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
        configuration.addKotlinSourceRoots(sources.map { it.absolutePath })
        configuration.addJvmClasspathRoots(classpath.toList())

        val environment = KotlinCoreEnvironment.createForProduction(
            disposable,
            configuration,
            EnvironmentConfigFiles.JVM_CONFIG_FILES,
        )

        val project = environment.project
        FirExtensionRegistrarAdapter.registerExtension(project, KonditionFirExtensionRegistrar())

        val localFileSystem = VirtualFileManager.getInstance().getFileSystem(StandardFileSystems.FILE_PROTOCOL)

        val files = buildList {
            for (source in sources) {
                source.walkTopDown().filter { it.isFile }.forEach {
                    this += localFileSystem.findFileByPath(it.absolutePath)!!
                }
            }
        }

        val sourceFiles = files.mapTo(mutableSetOf(), ::KtVirtualFileSourceFile)

        val input = ModuleCompilerInput(
            targetId = TargetId(JvmProtoBufUtil.DEFAULT_MODULE_NAME, targetName),
            groupedSources = GroupedKtSources(
                platformSources = sourceFiles,
                commonSources = emptyList(),
                sourcesByModuleName = mapOf(JvmProtoBufUtil.DEFAULT_MODULE_NAME to sourceFiles),
            ),
            commonPlatform = CommonPlatforms.defaultCommonPlatform,
            platform = JvmPlatforms.unspecifiedJvmPlatform,
            configuration = configuration,
        )

        val reporter = spyk(DiagnosticReporterFactory.createPendingReporter()) {

        }
        val globalScope = GlobalSearchScope.allScope(project)
        val packagePartProvider = environment.createPackagePartProvider(globalScope)
        val projectEnvironment = VfsBasedProjectEnvironment(
            project = project,
            localFileSystem = localFileSystem,
            getPackagePartProviderFn = { packagePartProvider },
        )

        val result = compileModuleToAnalyzedFir(
            input = input,
            projectEnvironment = projectEnvironment,
            previousStepsSymbolProviders = emptyList(),
            incrementalExcludesScope = null,
            diagnosticsReporter = reporter,
        )

        reporter.diagnostics.filter {
            it.severity == Severity.ERROR
        }.forEach {
            println(it)
        }


        println(result.outputs.map {
            it.fir.map {
                it.declarations.filterIsInstance<FirFunction>().map { it.nameOrSpecialName }
            }
        })
        println(result.outputs.map { it.fir })
    }

    @AfterTest
    fun tearDown() {
        unmockkAll()
    }
}
