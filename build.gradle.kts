plugins {
    java
    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

allprojects {
    group = "pw.dotdash"
    version = "0.5.0"
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(project(":arven-towns-api"))

    runtime(kotlin("stdlib-jdk8"))
    runtime(kotlin("reflect"))

    runtime(project(":arven-towns-api"))

    compileOnly("org.spongepowered:spongeapi:7.1.0")
    kapt("org.spongepowered:spongeapi:7.1.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

val shadowJar: com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar by tasks

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    shadowJar {
        archiveClassifier.set("dist")

        relocate("kotlin", "arvenwood.towns.plugin.lib.kotlin")
        relocate("org.jetbrains", "arvenwood.towns.plugin.lib.jetbrains")

        minimize {
            exclude(project(":arven-towns-api"))
        }
    }
}

artifacts {
    add("archives", shadowJar)
}