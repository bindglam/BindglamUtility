subprojects {
    apply(plugin = "java-library")

    group = "com.bindglam"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.devs.beer/")
        maven("https://repo.nexomc.com/releases")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        maven("https://repo.codemc.org/repository/maven-public/")
    }
}