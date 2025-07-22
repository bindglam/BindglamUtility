plugins {
    id("com.gradleup.shadow") version "9.0.0-rc1"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.lone:api-itemsadder:4.0.10")
    compileOnly("com.nexomc:nexo:1.8.0")
    implementation("dev.jorel:commandapi-bukkit-shade:10.1.0")

    rootProject.project("nms").subprojects.forEach {
        if(it.name == "api")
            implementation(project(":nms:${it.name}"))
        else
            implementation(project(":nms:${it.name}", configuration = "reobf"))
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName = "BindglamUtility.jar"

        relocate("dev.jorel.commandapi", "com.bindglam.utility.shaded.commandapi")
    }
}