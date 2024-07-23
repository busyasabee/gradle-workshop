package workshop

import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import javax.inject.Inject

@CacheableTask
abstract class CompileTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceDir: DirectoryProperty

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val classpath: ConfigurableFileCollection

    @get:Input
    @get:Optional
    abstract val javaVersion: Property<JavaVersion>

    @get:Input
    @get:Optional
    abstract val additionalArgs: ListProperty<String>

    @get:OutputDirectory
    abstract val classDir: DirectoryProperty // output directory after compilation

    @get:Inject
    abstract val execOperations: ExecOperations
    @get:Inject
    abstract val fileOperations: FileOperations

    init {
        println("create compile")
    }

    @TaskAction
    fun compile() {
        fileOperations.delete(classDir  )
        execOperations.exec {
            executable("javac")
            javaVersion.orNull?.let {
                args("--release", it.toString())
            }
            args("--source-path", sourceDir.get().asFile.path)
            if (!classpath.isEmpty) {
                args("--class-path", classpath.asPath)
            }
            args("-d", classDir.get().asFile.path)
            args(additionalArgs.get())
            args(sourceDir.get().asFileTree)
        }
    }
}