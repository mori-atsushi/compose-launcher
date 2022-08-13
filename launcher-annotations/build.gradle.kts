plugins {
    kotlin("jvm")
}

sourceSets {
    getByName("main") { java.srcDirs("src/main/kotlin") }
    getByName("test") { java.srcDirs("src/test/kotlin") }
}

dependencies {
    implementation(kotlin("stdlib"))
}
