plugins {
    id("standard-conventions")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade:10.1.0")

    rootProject.project("nms").subprojects.forEach {
        if(it.name == "api")
            implementation(project(":nms:${it.name}"))
        else
            implementation(project(":nms:${it.name}", configuration = "reobf"))
    }
}