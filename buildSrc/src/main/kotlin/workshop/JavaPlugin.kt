package workshop

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class JavaPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        apply(plugin = "base")
        val ext = extensions.create<JavaPluginsExtension>("java")
        val implementation = configurations.dependencyScope("implementation")
        val runtimeClasspath = configurations.resolvable("runtimeClasspath") {
            extendsFrom(implementation.get())
        }
        val compileTask = tasks.register("compile", CompileTask::class.java) {
            sourceDir.set(layout.projectDirectory.dir("src/main/java"))
            classDir.set(layout.buildDirectory.dir("$name/classes"))
            classpath.from(runtimeClasspath)
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
    }
}