plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.github.markdown.toolbar"
version = "1.0.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3.4")
    type.set("IC")
    plugins.set(listOf("org.intellij.plugins.markdown"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set(provider { null as String? }) // Remove o limite superior de versão
    }
    // Desativa a indexação de opções de configurações
    buildSearchableOptions {
        enabled = false
    }
}