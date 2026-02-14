plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.qubit.core_vhal"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.register<Jar>("createJar") {
    from(android.sourceSets["main"].java.srcDirs)
    from("build/intermediates/javac/release/classes")

    archiveFileName.set("core_vhal.jar")
    destinationDirectory.set(file("${layout.buildDirectory}/libs"))
}

val carJarPath = project.findProperty("android.car.jar.path") as String

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.hilt.android)
    testImplementation(libs.io.mock)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    compileOnly(files("${android.sdkDirectory}/$carJarPath"))
    testImplementation(files("${android.sdkDirectory}/$carJarPath"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
}