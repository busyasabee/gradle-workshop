package workshop

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create

class JavaPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        apply(plugin = "base")
        tasks.register("compile", CompileTask::class.java) {
            sourceDir.set(layout.projectDirectory.dir("src/main/java"))
            classDir.set(layout.buildDirectory.dir("$name/classes"))
        }
    }
}