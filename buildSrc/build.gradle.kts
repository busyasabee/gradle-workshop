plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("java") {
            id = "workshop.java"
            implementationClass = "workshop.JavaPlugin"
        }
    }
}
