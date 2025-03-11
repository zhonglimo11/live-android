plugins {
    id("com.android.library")
}

android {
    compileSdk = 33
    buildToolsVersion = "25.0.2"

    defaultConfig {
        minSdk = 14
        testOptions {
            targetSdk = 28  // 修改这里
        }
        lint {
            targetSdk = 28  // 修改这里
        }
        namespace = "com.zaaach.citypicker"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isMinifyEnabled = false // 禁用混淆
            isShrinkResources = false // 禁用资源压缩
        }
    }
}

dependencies {
    api(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    androidTestImplementation("com.android.support.test.espresso:espresso-core:2.2.2") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    api("com.android.support:appcompat-v7:24.2.1")
    testImplementation("junit:junit:4.12")
    api(files("libs/AMap_Location_V3.5.0_20170731.jar"))
}
