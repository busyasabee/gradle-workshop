plugins {
    workshop.java
}

java {
    version = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":producer"))
//    implementation("org.apache.commons:commons-text:1.11.0")
//    implementation("com.google.guava:guava:33.1.0-jre")
}

tasks.compile {
    additionalArgs.add("-parameters")
}

tasks.runMain {
    mainClass = "workshop.Consumer"
}