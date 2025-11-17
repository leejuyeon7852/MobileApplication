plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    /*ROOM dependency 관련 정보 추가*/
}

android {
    namespace = "ddwu.com.mobile.miniproject2"
    compileSdk = 36

    defaultConfig {
        applicationId = "ddwu.com.mobile.miniproject2"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding.enable=true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Google play service 위치 관련 정보 추가
    
    // GoogleMap 관련 정보 추가 

    /*ROOM dependency 관련 정보 추가*/
}