rootProject.name = "gradle-workshop"

include(
    "consumer",
    "producer",
//    "spring-app",
)

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}