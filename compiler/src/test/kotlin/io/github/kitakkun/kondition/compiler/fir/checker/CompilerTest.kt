package io.github.kitakkun.kondition.compiler.fir.checker

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.VfsBasedProjectEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.openapi.util.text.StringUtilRt
import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFileManager
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.PsiFileFactoryImpl
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.DiagnosticReporterFactory
import org.jetbrains.kotlin.fir.FirModuleDataImpl
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirFunctionChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.java.FirProjectSessionProvider
import org.jetbrains.kotlin.fir.pipeline.buildResolveAndCheckFirFromKtFiles
import org.jetbrains.kotlin.fir.render
import org.jetbrains.kotlin.fir.session.FirCommonSessionFactory
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.CommonPlatforms
import org.jetbrains.kotlin.psi.KtFile
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.test.Test

class CompilerTest {

    object TestRegistrar : FirExtensionRegistrar() {
        override fun ExtensionRegistrarContext.configurePlugin() {
            +::TestAdditionalCheckerExtension
        }
    }

    object TestChecker : FirFunctionChecker(mppKind = MppCheckerKind.Common) {
        override fun check(declaration: FirFunction, context: CheckerContext, reporter: DiagnosticReporter) {
            error("GHEOGHOE")
        }
    }

    class TestAdditionalCheckerExtension(session: FirSession) : FirAdditionalCheckersExtension(session) {
        override val declarationCheckers: DeclarationCheckers
            get() = object : DeclarationCheckers() {
                override val functionCheckers: Set<FirFunctionChecker>
                    get() = setOf(TestChecker)
            }
    }

    @Test
    fun testte() {
        val disposable = Disposer.newDisposable()
        val configuration = CompilerConfiguration()
        val moduleName = Name.identifier("myModule")
        configuration.put(CommonConfigurationKeys.MODULE_NAME, moduleName.asString())
        configuration.put(CommonConfigurationKeys.USE_FIR, true)
        val coreEnvironment = KotlinCoreEnvironment.createForProduction(
            disposable,
            configuration,
            EnvironmentConfigFiles.JVM_CONFIG_FILES,
        )

        // FirSession
        val moduleData = FirModuleDataImpl(
            name = moduleName,
            dependencies = emptyList(),
            dependsOnDependencies = emptyList(),
            friendDependencies = emptyList(),
            platform = CommonPlatforms.defaultCommonPlatform,
        )
        val projectEnvironment = VfsBasedProjectEnvironment(
            project = coreEnvironment.project,
            localFileSystem = VirtualFileManager.getInstance().getFileSystem(StandardFileSystems.FILE_PROTOCOL),
            getPackagePartProviderFn = coreEnvironment::createPackagePartProvider,
        )
//        val librariesScope = PsiBasedProjectFileSearchScope(ProjectScope.getLibrariesScope(coreEnvironment.project))

        val firSession = FirCommonSessionFactory.createModuleBasedSession(
            moduleData = moduleData,
            sessionProvider = FirProjectSessionProvider(),
            projectEnvironment = projectEnvironment,
            incrementalCompilationContext = null,
            extensionRegistrars = FirExtensionRegistrar.getInstances(coreEnvironment.project) + TestRegistrar,
            languageVersionSettings = configuration.languageVersionSettings,
            lookupTracker = configuration.get(CommonConfigurationKeys.LOOKUP_TRACKER),
            enumWhenTracker = configuration.get(CommonConfigurationKeys.ENUM_WHEN_TRACKER),
            importTracker = configuration.get(CommonConfigurationKeys.IMPORT_TRACKER),
            registerExtraComponents = {},
            init = {},
        )

        val kotlinFile = File.createTempFile("inmemory", ".kt")
        kotlinFile.writeText(
            """
                fun hoge() { }
            """.trimIndent()
        )
        val output = buildResolveAndCheckFirFromKtFiles(
            ktFiles = listOf(
                kotlinFile.toKtFile(coreEnvironment.project),
            ),
            session = firSession,
            diagnosticsReporter = DiagnosticReporterFactory.createReporter(),
        )

        output.fir.forEach {
            println(it.render())
        }
    }

    fun File.toKtFile(project: Project): KtFile {
        val shortName = name.substring(name.lastIndexOf('/') + 1).let {
            it.substring(it.lastIndexOf('\\') + 1)
        }

        val virtualFile = object : LightVirtualFile(
            shortName,
            KotlinLanguage.INSTANCE,
            StringUtilRt.convertLineSeparators(readText())
        ) {
            override fun getPath(): String = "${this@toKtFile.path}/$name"
        }

        virtualFile.charset = StandardCharsets.UTF_8
        val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
        val ktFile = factory.trySetupPsiForFile(
            virtualFile, KotlinLanguage.INSTANCE, true, false
        ) as KtFile

        return ktFile
    }
}
