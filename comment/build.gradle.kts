plugins {
    id("com.android.library")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        testOptions {
            targetSdk = 26
        }
        lint {
            targetSdk = 26
        }
        namespace = "com.sun.resources"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

repositories {
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // implementation fileTree(mapOf("include" to listOf("*.arr"), "dir" to "libs"))
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    // 此处解析假数据
    implementation("com.alibaba:fastjson:1.1.46.android")
    // 加载图片
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // BaseRecyclerViewAdapterHelper 3.0大版本 BaseNodeAdapter有bug，使用另一个网友更改的，下载有时会不好用，使用之前缓存的arr
    // implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.11")
    // implementation("com.github.jqorz:BaseRecyclerViewAdapterHelper:3.0.6.1")
    // implementation(name = "BaseRecyclerViewAdapterHelper-3.0.6.1", ext = "aar")
    implementation(files("libs/BaseRecyclerViewAdapterHelper-3.0.6.1.aar"))

    implementation("com.mapzen:on-the-road:0.8.4")
    implementation("com.android.support:multidex:1.0.3")
    // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.2.31")
}
