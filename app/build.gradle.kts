plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.eden"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eden"
        minSdk = 30
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.auth)
    implementation(libs.activity)
    implementation(libs.firebase.storage)
    implementation(libs.camera.core)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.cardview)

    implementation(libs.material)
    implementation(libs.glide) // Glide

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation(libs.viewpager2)
    implementation(libs.dotsindicator)

    // Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.libraries.places:places:4.0.0")

    // Volley (Required for maps)
    implementation("com.android.volley:volley:1.2.1")

    // ImagePicker by Dhaval2404
    implementation(libs.imagepicker)

    // Google Material
    implementation(libs.material.v160)

    implementation(libs.libphonenumber)

    // Lombok
//    implementation("org.projectlombok:lombok:1.18.28")
//    implementation("org.projectlombok:lombok:1.18.28")

}

configurations.all {
    resolutionStrategy {
        force("com.google.android.gms:play-services-location:21.0.1")
        force("com.google.android.libraries.places:places:4.0.0")
    }
}