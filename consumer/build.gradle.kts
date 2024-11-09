plugins {
    workshop.`java-convention`
    workshop.dbplugin
}

dependencies {
    implementation(project(":producer"))
    implementation("org.postgresql:postgresql:42.7.4")
}

tasks.runMain {
    mainClass = "workshop.Consumer"
}
