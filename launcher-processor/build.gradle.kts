plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
}

sourceSets {
    getByName("main") {
        java.srcDirs("src/main/kotlin")
    }
    getByName("test") {
        java.srcDirs("src/test/kotlin")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    implementation(project(":launcher-annotations"))

    testImplementation(kotlin("test"))
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.9")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.9")
}
