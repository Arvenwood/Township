plugins {
    java
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven")
}

dependencies {
    compileOnly("org.spongepowered:spongeapi:7.1.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}