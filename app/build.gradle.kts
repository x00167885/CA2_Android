plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.eventplanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eventplanner"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    // Including necessary JSON serialisation dependency:
    implementation("com.google.code.gson:gson:2.10.1")
    // Including retrofit and okhttp libraries for simplifying network requests.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson converter for Retrofit
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    // Including swipe to refresh so to allow the user to refresh content.
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    // Including expresso testing framework for testing various activities.
    implementation(libs.espresso.contrib)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")
    // Including Junit for regular tests not associated with activity components / views.
    testImplementation(libs.junit)
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
}