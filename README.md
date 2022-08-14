# Jetpack Compose Launcher

## Goodbye Activity

When you're develop an Android application only with [Jetpack Compose](https://developer.android.com/jetpack/compose), you probably don't need to aware about Activities.

This library automatically create the Activity class from a Composable functions with an `Entry` annotation with [KSP](https://github.com/google/ksp).

You can start development quickly without touching the Activity or the AndroidManifest.

## Getting Started

### 1. Create new project without Activity

Select `No Activity` from `Android Studio > File > New > New Project`.

![android studio wizard](https://user-images.githubusercontent.com/13435109/184525219-c262ba62-b0db-4e2e-8f60-cff65232322d.png)

### 2. Add dependencies

Add Maven Central repository to your `build.gradle`.

```kotlin
repositories {
    mavenCentral()
}
```

Add the package dependencies to your `build.gradle`.

You'll need to enable KSP.

```kotlin
plugins {
    // use latest version
    id("com.google.devtools.ksp").version("1.7.10-1.0.6")
}

dependencies {
    implementation("com.moriatsushi.launcher:launcher:1.0.0-dev03")
    ksp("com.moriatsushi.launcher:launcher-processor:1.0.0-dev03")
}
```

Add Jetpack Compose dependencies according to your needs.

```kotlin
android {
    // ...

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    composeOptions {
        // use latest version
        kotlinCompilerExtensionVersion = 1.1.0
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // ...

    // use latest version
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
}
```

### 3. Write Composable function with `Entry` annotation
```kotlin
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.moriatsushi.launcher.Entry

@Entry
@Composable
fun Main() {
    MaterialTheme {
        Scaffold {
            /* ... */
        }
    }
}
```

## Notice
* Multiple entries is not supported. Issue: [#1](https://github.com/Mori-Atsushi/compose-launcher/issues/1)
