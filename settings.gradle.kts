plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "BindglamUtility"
include("core")
include("nms:v1_21_R3")
include("nms:v1_21_R5")
include("api")
