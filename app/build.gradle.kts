plugins {
    id("com.android.application")
}

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
        versionName = "0.1.0-dev"
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
        checkDependencies = true
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
