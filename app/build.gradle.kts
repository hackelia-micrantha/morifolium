import org.gradle.api.tasks.testing.Test

plugins {
    id("com.android.application")
}

val morifoliumVersion = rootProject.file(".morifolium-version").readText().trim()
check(morifoliumVersion.isNotEmpty()) { ".morifolium-version must not be empty" }

android {
    namespace = "com.micrantha.morifolium"

    // API 37 is visible to lint as a newer preview level while this reference
    // profile deliberately targets the current stable Android 16 / API 36 SDK.
    //noinspection GradleDependency
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "com.micrantha.morifolium.reference"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = 1
        versionName = morifoliumVersion
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
        checkDependencies = true
    }
}

tasks.withType<Test>().configureEach {
    systemProperty("morifolium.repoRoot", rootProject.projectDir.absolutePath)
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
