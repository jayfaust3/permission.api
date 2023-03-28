import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.10"
    kotlin("plugin.jpa") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
}

group = "com.permission.api"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-web:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
    implementation("org.springframework.vault:spring-vault-dependencies:3.0.0")
    implementation("org.springframework.vault:spring-vault-core:3.0.0")
    implementation("org.springframework.data:spring-data-commons:3.0.3")

    // kotlinx
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    //aws
    implementation("com.amazonaws:aws-java-sdk:1.12.429")

    // rabbit-mq
    implementation("org.springframework:spring-messaging:6.0.6")
    implementation("org.springframework.amqp:spring-rabbit:3.0.2")
    implementation("org.springframework.boot:spring-boot-starter:2.7.9")
    implementation("org.springframework.boot:spring-boot-starter-amqp:3.0.4")

    // arrow
    implementation("io.arrow-kt:arrow-core-jvm:1.1.5")

    // jwt / auth0
    implementation("com.auth0:auth0-spring-security-api:1.5.2")
    implementation("io.jsonwebtoken:jjwt-extensions:0.11.5")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}