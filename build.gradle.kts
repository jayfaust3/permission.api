import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
}

group = "com.permission.api"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.vault:spring-vault-dependencies:2.3.2")
    implementation("org.springframework.vault:spring-vault-core:2.3.2")
    implementation("org.springframework.data:spring-data-commons:2.3.3.RELEASE")

    //aws
    implementation("com.amazonaws:aws-java-sdk:1.12.403")

    // nimbus
    implementation("com.nimbusds:nimbus-jose-jwt:9.22")

    // jackson json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // arrow
    implementation("io.arrow-kt:arrow-core-jvm:1.1.2")

    // jwt / auth0
    implementation("com.auth0:auth0-spring-security-api:1.5.1")
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
        jvmTarget = "11"
    }
}