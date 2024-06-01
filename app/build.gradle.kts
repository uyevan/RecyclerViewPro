plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.evan.pro.recycler"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.evan.pro.recycler"
        minSdk = 24
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
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //LomBok
    implementation(libs.org.projectlombok.lombok4)
    annotationProcessor(libs.org.projectlombok.lombok4)
    //Glide
    implementation(libs.glide)
    //下拉刷新
    implementation(libs.refresh.layout.kernel)
    implementation(libs.github.refresh.header.classics)
    implementation(libs.refresh.footer.classics)
}