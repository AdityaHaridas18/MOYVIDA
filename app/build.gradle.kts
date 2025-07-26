plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.moyvida.moyvida"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moyvida.moyvida"
        minSdk = 27
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.hbb20:ccp:2.6.1")
    implementation("com.google.android.gms:play-services-safetynet:18.1.0")
    implementation ("androidx.browser:browser:1.4.0" )
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation ("androidx.cardview:cardview:1.0.0")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Dots indicator
    implementation("com.tbuonomo:dotsindicator:4.2")

    // ExoPlayer
    implementation("com.google.android.exoplayer:exoplayer:2.17.1")

    // Glide for image loading
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.firebase.auth)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}