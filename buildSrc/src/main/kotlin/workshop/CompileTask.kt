package workshop

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class CompileTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceDir: DirectoryProperty

    @get:OutputDirectory
    abstract val classDir: DirectoryProperty // output directory after compilation

    @get:Inject
    abstract val execOperations: ExecOperations

    @TaskAction
    fun compile() {
        execOperations.exec {
            executable("javac")
            args("--source-path", sourceDir.get().asFile.path)
            args("-d", classDir.get().asFile.path)
            args(sourceDir.get().asFileTree)
        }
    }
}