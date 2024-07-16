plugins {
    workshop.java
}

java {
    version = JavaVersion.VERSION_17
}

tasks.compile {
    additionalArgs.add("-parameters")
}