plugins {
    id("com.android.application")
}

android {
    namespace = "com.micrantha.morifolium"
    compileSdk = 37
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "com.micrantha.morifolium.reference"
        minSdk = 26
        targetSdk = 37
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
