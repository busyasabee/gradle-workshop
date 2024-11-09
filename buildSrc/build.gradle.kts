plugins {
    `kotlin-dsl`
}

dependencies {
    api("org.springframework.boot:spring-boot-gradle-plugin:3.2.4")
    implementation("org.postgresql:postgresql:42.7.4")

}

gradlePlugin {
    plugins {
        create("build-timestamp") {
            id = "workshop.build-timestamp"
            implementationClass = "workshop.BuildTimestampPlugin"
        }
        create("java") {
            id = "workshop.java"
            implementationClass = "workshop.JavaPlugin"
        }
        create("spring-boot") {
            id = "workshop.spring-boot"
            implementationClass = "workshop.SpringBootExtendedPlugin"
        }
        create("dbplugin") {
            id = "workshop.dbplugin"
            implementationClass = "workshop.DbPlugin"
        }
    }
}