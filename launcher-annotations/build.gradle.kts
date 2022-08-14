plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
}

sourceSets {
    getByName("main") { java.srcDirs("src/main/kotlin") }
    getByName("test") { java.srcDirs("src/test/kotlin") }
}

dependencies {
    implementation(kotlin("stdlib"))
}
