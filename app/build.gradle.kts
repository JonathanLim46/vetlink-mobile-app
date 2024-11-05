plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.vetlink"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vetlink"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }
    dataBinding{
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //UI
    implementation(libs.androidx.cardview)
    implementation(libs.hdodenhof.circleimageview)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(libs.androidx.fragment.ktx)


    //retrofit library
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")

    //gson converter library
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.11.0")

    //okhttp library
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    //coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    //ViewPager
    implementation ("androidx.viewpager2:viewpager2:1.1.0")

    //GPS Manager
    implementation("com.google.android.gms:play-services-location:21.3.0")

    

}