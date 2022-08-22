import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "savemenow.es.protecciontotalresk.android"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }


}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    //Firebase Libs
    implementation ("com.google.firebase:firebase-bom:29.0.0")
    implementation ("com.google.firebase:firebase-auth:21.0.7")
    implementation ("com.google.firebase:firebase-database:20.0.5")
    implementation ("com.google.firebase:firebase-core:20.1.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.2.2")
    implementation ("com.google.android.gms:play-services-auth:20.2.0")
    //Facebook
    implementation ("com.facebook.android:facebook-android-sdk:4.18.0")
    implementation ("com.facebook.android:facebook-android-sdk:4.18.0")
    implementation ("com.facebook.android:facebook-android-sdk:[8,9)")
}