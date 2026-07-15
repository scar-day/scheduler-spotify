plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.scarday"
version = "0.0.1-SNAPSHOT"
description = "telegram-spotify"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    implementation("com.github.ben-manes.caffeine:caffeine:3.2.4")

    implementation("se.michaelthelin.spotify:spotify-web-api-java:9.1.0")

    implementation("org.telegram:telegrambots-longpolling:10.0.0")
    implementation("org.telegram:telegrambots-client:10.0.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

val dockerImage = "scarday/scheduler-spotify"
val dockerTag = project.findProperty("dockerTag")?.toString() ?: "latest"

tasks.register<Exec>("dockerBuild") {
    group = "docker"
    description = "Builds the docker image ($dockerImage:$dockerTag)"
    dependsOn(tasks.named("bootJar"))
    commandLine("docker", "build", "-t", "$dockerImage:$dockerTag", ".")
}

tasks.register<Exec>("dockerPush") {
    group = "docker"
    description = "Pushes the docker image ($dockerImage:$dockerTag) to the registry"
    dependsOn(tasks.named("dockerBuild"))
    commandLine("docker", "push", "$dockerImage:$dockerTag")
}