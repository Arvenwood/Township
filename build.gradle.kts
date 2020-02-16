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
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(project(":township-api"))

    implementation("com.github.TheFrontier.director:director-core:aab35a1e45")
    implementation("com.github.TheFrontier.director:director-sponge:aab35a1e45")

    runtime(kotlin("stdlib-jdk8"))
    runtime(kotlin("reflect"))

    runtime(project(":township-api"))

    runtime("com.github.TheFrontier.director:director-core:aab35a1e45")
    runtime("com.github.TheFrontier.director:director-sponge:aab35a1e45")

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

        relocate("pw.dotdash.director", "pw.dotdash.township.plugin.lib.director")
        relocate("kotlin", "pw.dotdash.township.plugin.lib.kotlin")
        relocate("org.jetbrains", "pw.dotdash.township.plugin.lib.jetbrains")

        minimize {
            exclude(project(":township-api"))
        }
    }
}

artifacts {
    add("archives", shadowJar)
}