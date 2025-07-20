plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.doandidong"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.doandidong"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions{
        exclude ("META-INF/DEPENDENCIES")
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
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //glider
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    //RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.0")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    // bragde
    implementation ("com.nex3z:notification-badge:1.0.4")
    //even bus
    implementation ("org.greenrobot:eventbus:3.0.0")
    //paper
    implementation ("io.github.pilgr:paperdb:2.7.2")
    //postdatagson
    implementation ("com.google.code.gson:gson:2.13.1")
    //latte
    implementation ("com.airbnb.android:lottie-compose:6.6.6")
    // The BOM will manage the module versions and transitive dependencies
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    // lammomo
    implementation ("com.github.momo-wallet:mobile-sdk:1.0.7")
    //youtube
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")















}