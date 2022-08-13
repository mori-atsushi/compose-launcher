plugins {
    kotlin("jvm")
}

sourceSets {
    getByName("main") { java.srcDirs("src/main/kotlin") }
    getByName("test") { java.srcDirs("src/test/kotlin") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    implementation(project(":launcher-annotations"))
}
