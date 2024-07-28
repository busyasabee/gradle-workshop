plugins {
//    workshop.java
    `java-convention`
}

//java {
//    version = JavaVersion.VERSION_17
//}

dependencies {
    api("org.apache.commons:commons-text:1.11.0")
    api("com.google.guava:guava:33.1.0-jre")
}

//tasks.compile {
//    additionalArgs.add("-parameters")
//}

tasks.runMain {
    mainClass = "workshop.Producer"
}