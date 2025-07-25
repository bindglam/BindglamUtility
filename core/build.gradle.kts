plugins {
    id("standard-conventions")
}

dependencies {
    implementation(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade:10.1.2")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.57")
    implementation("com.zaxxer:HikariCP:6.3.2")

    rootProject.project("nms").subprojects.forEach {
        implementation(project(":nms:${it.name}", configuration = "reobf"))
    }
}