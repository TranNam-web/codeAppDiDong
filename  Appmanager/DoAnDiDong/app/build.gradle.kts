plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)




}

android {
    namespace = "com.manager.doandidong" // ✅ Đã sửa đúng với package của code
    compileSdk = 35

    defaultConfig {
        applicationId = "com.manager.doandidong"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
        dataBinding = true

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

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // RxJava
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // Notification badge
    implementation("com.nex3z:notification-badge:1.0.4")

    // EventBus
    implementation("org.greenrobot:eventbus:3.0.0")

    // PaperDB
    implementation("io.github.pilgr:paperdb:2.7.2")

    // Gson
    implementation("com.google.code.gson:gson:2.13.1")

    // Lottie (nếu đang dùng Jetpack Compose)
    implementation("com.airbnb.android:lottie-compose:6.6.6")
    //neumorphirm
    implementation("com.github.fornewid:neumorphism:0.3.0")
    //image pick
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    //VeDoThi
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.google.android.material:material:1.11.0")

}
