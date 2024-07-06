package workshop

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.util.Trie.from
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class JavaPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        apply(plugin = "base")
        val compileTask = tasks.register("compile", CompileTask::class.java) {
            sourceDir.set(layout.projectDirectory.dir("src/main/java"))
            classDir.set(layout.buildDirectory.dir("$name/classes"))
        }

        tasks.register<Jar>("jar") {
            from(compileTask/*.flatMap { it.classDir }*/)
            destinationDirectory.set(layout.buildDirectory.dir("libs"))
//            into(layout.buildDirectory.dir("libs"))
        }
    }
}