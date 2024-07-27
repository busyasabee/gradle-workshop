package workshop

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.java.TargetJvmEnvironment
import org.gradle.api.attributes.java.TargetJvmVersion
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import javax.inject.Inject
import workshop.attributes.*

class JavaPlugin @Inject constructor(private val softwareComponentFactory: SoftwareComponentFactory): Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        apply(plugin = "base")

        dependencies {
            attributesSchema {
                attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE) {
                    compatibilityRules.add(JavaVersionCompatibilityRule::class)
                }
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE) {
                    compatibilityRules.add(LibraryElementsCompatibilityRules::class)
                }
                attribute(TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE) {
                    disambiguationRules.add(TargetJvmEnvironmentDisambiguationRule::class)
                }
            }
        }
        val ext = extensions.create<JavaPluginsExtension>("java")
        val api = configurations.dependencyScope("api")
        val implementation = configurations.dependencyScope("implementation") {
            extendsFrom(api.get())
        }
        val runtimeClasspath = configurations.resolvable("runtimeClasspath") {
            extendsFrom(implementation.get())
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
                attributeProvider(
                    TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE,
                    ext.version.map { it.majorVersion.toInt()}
                )
            }
        }
        val compileClasspath = configurations.resolvable("compileClasspath") {
            extendsFrom(implementation.get())
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
                attributeProvider(
                    TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE,
                    ext.version.map { it.majorVersion.toInt()}
                )
            }
        }
        val compileTask = tasks.register("compile", CompileTask::class.java) {
            sourceDir.set(layout.projectDirectory.dir("src/main/java"))
            classDir.set(layout.buildDirectory.dir("$name/classes"))
            classpath.from(compileClasspath)
        }

        val jarTask = tasks.register<Jar>("jar") {
            from(compileTask/*.flatMap { it.classDir }*/)
            destinationDirectory.set(layout.buildDirectory.dir("libs"))
//            into(layout.buildDirectory.dir("libs"))
        }

        val runMainTask = tasks.register<JavaExec>("runMain") {
            classpath(compileTask, runtimeClasspath)
        }

        tasks.named("assemble") {
            dependsOn(jarTask)
        }

        configurations.consumable("runtimeElements") {
            extendsFrom(implementation.get())
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
                attributeProvider(
                    TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE,
                    ext.version.map { it.majorVersion.toInt()}
                )
            }
            outgoing.artifact(jarTask)
        }

        configurations.consumable("apiElements") {
            extendsFrom(api.get())
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API ))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
                attributeProvider(
                    TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE,
                    ext.version.map { it.majorVersion.toInt()}
                )
            }
            outgoing.artifact(jarTask)
        }

        softwareComponentFactory.adhoc("java").apply {
            addVariantsFromConfiguration(runtimeClasspath.get()) {
                mapToMavenScope("runtime")
            }
        }
    }
}