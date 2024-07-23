plugins {
    workshop.java
}

java {
    version = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.apache.commons:commons-text:1.11.0")
}

tasks.compile {
    additionalArgs.add("-parameters")
}

tasks.runMain {
    mainClass = "workshop.Consumer"
}