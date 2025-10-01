plugins {
    id("standard-conventions")
    id("com.gradleup.shadow") version "9.0.0-rc1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

dependencies {
    implementation(project(":core"))
    fun searchAll(target: Project) {
        val sub = target.subprojects
        if (sub.isNotEmpty()) sub.forEach {
            searchAll(it)
        }
    }
    searchAll(rootProject)
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }

    jar {
        finalizedBy(shadowJar)
    }

    shadowJar {
        archiveClassifier = ""
        dependencies {
            exclude(dependency("org.jetbrains:annotations:26.0.2"))
            exclude(dependency("org.jetbrains:annotations:24.1.0"))
            exclude(dependency("org.jetbrains:annotations:13.0"))
            exclude(dependency("org.slf4j:slf4j-api:2.0.9"))
            exclude(dependency("org.slf4j:slf4j-api:2.0.16"))
            exclude(dependency("org.slf4j:slf4j-api:2.0.17"))
        }
        fun prefix(pattern: String) {
            relocate(pattern, "com.bindglam.utility.shaded.$pattern")
        }
        prefix("kotlin")
        prefix("dev.jorel.commandapi")
        prefix("com.zaxxer.hikari")
        prefix("com.alibaba.fastjson2")
        prefix("redis.clients.jedis")
    }
}