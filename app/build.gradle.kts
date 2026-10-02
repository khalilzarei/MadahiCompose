plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.khz.madahi"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.khz.madahi"
        minSdk = 26
        targetSdk = 37
        versionCode = 11
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["TapsellMediationAppKey"] = "gcjhmacmancattfcaeqtqgtdpqckkjrmgefephhlsaadnjppidndrilmbkpdticrcigihl"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true  // ✅ این خط را اضافه کنید
        aidl = true          // ✅ فعال‌سازی AIDL برای سرویس پرداخت بازار
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
//    implementation(libs.core.ktx)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.runtime.livedata)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.material)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.aesthetic.dialogs)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.material)
    implementation(libs.smart.exception.java)

    implementation(libs.tapsell.plus.sdk.android)
}

ksp {
    arg(
        "room.schemaLocation",
        "$projectDir/schemas"
    )
}