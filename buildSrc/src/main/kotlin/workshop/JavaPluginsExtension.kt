package workshop

import org.gradle.api.JavaVersion
import org.gradle.api.provider.Property

abstract class JavaPluginsExtension {
    abstract val version: Property<JavaVersion>

    init {
        version.convention(JavaVersion.current())
    }
}