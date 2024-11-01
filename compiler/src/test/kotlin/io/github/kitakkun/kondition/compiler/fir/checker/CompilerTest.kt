package io.github.kitakkun.kondition.compiler.fir.checker

import io.github.kitakkun.kondition.compiler.fir.KonditionFirExtensionRegistrar
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.computeKotlinPaths
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.toAbstractProjectEnvironment
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.cli.jvm.config.configureJdkClasspathRoots
import org.jetbrains.kotlin.cli.jvm.configureStandardLibs
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.openapi.util.text.StringUtilRt
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
//        configuration.addKotlinSourceRoot("/Users/kitakkun/Documents/GitHub/kotlin/kondition/core/src/kotlin", isCommon = true)
        configuration.addJvmClasspathRoot(File("/Users/kitakkun/Documents/GitHub/kotlin/kondition/core/build/libs/core-jvm.jar"))
        configuration.configureJdkClasspathRoots()
        val messageCollector = MessageCollector.NONE
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
        val arguments = K2JVMCompilerArguments()
        configuration.configureStandardLibs(computeKotlinPaths(messageCollector, arguments), arguments)
//        configuration.configureStandardLibs(KotlinPathsFromBaseDirectory(),K2JVMCompilerArguments())
//        configuration.addKotlinSourceRoots(
//            listOf("/Users/kitakkun/Documents/GitHub/kotlin/kondition/core/src")
//        )

        val environment = KotlinCoreEnvironment.createForProduction(
            projectDisposable = disposable,
            configuration = configuration,
            configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES,
        )

        val project = environment.project
        val abstractProjectEnvironment = environment.toAbstractProjectEnvironment()

//        val coreEnvironment = KotlinCoreEnvironment.createForProduction(
//            disposable,
//            configuration,
//            configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES,
//        )
//        coreEnvironment.addKotlinSourceRoots(listOf(File("../core/src/kotlin")))
//
//        val coreModule = BinaryModuleData.createDependencyModuleData(
//            name = Name.identifier("core"),
//            platform = CommonPlatforms.defaultCommonPlatform,
//        )
//
//        val coreLibraryScope = PsiBasedProjectFileSearchScope(ProjectScope.getLibrariesScope(coreEnvironment.project))
//
//        FirCommonSessionFactory.createLibrarySession(
//            mainModuleName = Name.identifier("core"),
//            sessionProvider = FirProjectSessionProvider(),
//            moduleDataProvider = SingleModuleDataProvider(coreModule),
//            projectEnvironment = coreEnvironment.toAbstractProjectEnvironment(),
//            extensionRegistrars = emptyList(),
//            librariesScope = coreLibraryScope,
//            resolvedKLibs = emptyList(),
//            packageAndMetadataPartProvider = JvmPackagePartProvider(
//                languageVersionSettings = configuration.languageVersionSettings,
//                scope = GlobalSearchScope.allScope(coreEnvironment.project),
//            ),
//            languageVersionSettings = configuration.languageVersionSettings,
//            registerExtraComponents = {},
//        )

        // FirSession
        val moduleData = FirModuleDataImpl(
            name = moduleName,
            dependencies = listOf(),
            dependsOnDependencies = emptyList(),
            friendDependencies = emptyList(),
            platform = CommonPlatforms.defaultCommonPlatform,
        )

        val firSession = FirCommonSessionFactory.createModuleBasedSession(
            moduleData = moduleData,
            sessionProvider = FirProjectSessionProvider(),
            projectEnvironment = abstractProjectEnvironment,
            incrementalCompilationContext = null,
            extensionRegistrars = FirExtensionRegistrar.getInstances(project) + KonditionFirExtensionRegistrar(),
            languageVersionSettings = configuration.languageVersionSettings,
            lookupTracker = configuration.get(CommonConfigurationKeys.LOOKUP_TRACKER),
            enumWhenTracker = configuration.get(CommonConfigurationKeys.ENUM_WHEN_TRACKER),
            importTracker = configuration.get(CommonConfigurationKeys.IMPORT_TRACKER),
            registerExtraComponents = {},
            init = { },
        )

        val annotationFiles = File("../core/src/commonMain/kotlin").walk()
            .filter { it.isFile && it.extension == "kt" }
            .map {
                it.toKtFile(project)
            }


        annotationFiles.forEach {
            println(it.virtualFilePath)
        }
        val kotlinFile = File.createTempFile("inmemory", ".kt")
        kotlinFile.writeText(
            """
                import io.github.kitakkun.kondition.core.annotation.NonEmpty
                
                fun hoge(@NonEmpty value: Int) { }
            """.trimIndent()
        )
        kotlinFile.toKtFile(project)

        val output = buildResolveAndCheckFirFromKtFiles(
//            ktFiles = listOf(kotlinFile.toKtFile(project)) + annotationFiles.toList(),
            ktFiles = annotationFiles.toList(),
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
            override fun getPath(): String = this@toKtFile.path
        }

        virtualFile.charset = StandardCharsets.UTF_8
        val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
        val ktFile = factory.trySetupPsiForFile(
            virtualFile, KotlinLanguage.INSTANCE, true, false
        ) as KtFile

        return ktFile
    }
}
