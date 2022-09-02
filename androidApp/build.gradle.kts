import org.jetbrains.kotlin.daemon.client.KotlinCompilerClient.compile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
    buildFeatures {
        viewBinding = true
    }

    dataBinding {
       isEnabled = true
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
    //Firebase ui

    // FirebaseUI for Firebase Realtime Database
    //implementation ("com.firebaseui:firebase-ui-database:8.0.1")

    // FirebaseUI for Cloud Firestore
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.1")

    // FirebaseUI for Firebase Auth
    //implementation 'com.firebaseui:firebase-ui-auth:8.0.1'

    // FirebaseUI for Cloud Storage
    //implementation 'com.firebaseui:firebase-ui-storage:8.0.1'

    //Facebook
    implementation ("com.facebook.android:facebook-android-sdk:4.18.0")
    implementation ("com.facebook.android:facebook-android-sdk:4.18.0")
    implementation ("com.facebook.android:facebook-android-sdk:[8,9)")
    //implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0")
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    implementation("com.google.android.gms:play-services-location:20.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")
    //Circle Image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //coroutines
    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation ("androidx.activity:activity-ktx:1.2.3")
    //hilt
    implementation ("com.google.dagger:hilt-android:2.43.2")
    kapt ("com.google.dagger:hilt-compiler:2.43.2")
    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.7.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.7.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //ktx
    implementation ("androidx.activity:activity-ktx:1.4.0")
    implementation ("androidx.fragment:fragment-ktx:1.4.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    //VCARD
    implementation ("com.googlecode.ez-vcard:ez-vcard:0.11.3")
    //QR
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    //Maps
    implementation ("com.google.maps.android:maps-ktx:3.2.1")
    implementation ("com.google.maps.android:maps-utils-ktx:3.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    // Dependency to include Maps SDK for Android
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:17.0.0")

}