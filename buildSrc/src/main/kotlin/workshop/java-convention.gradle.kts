import workshop.CompileTask
import workshop.JavaPluginsExtension

apply(plugin = "workshop.java")

extensions.configure<JavaPluginsExtension> {
    version = JavaVersion.VERSION_17
}

tasks.named<CompileTask>("compile") {
    additionalArgs.add("-parameters")
}