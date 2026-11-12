plugins {
    kotlin("jvm") version "2.2.20"
}

group = "de.hawkiel.ki.cant_stop.ai"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}