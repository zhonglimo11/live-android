plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.administrator.live"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.administrator.live"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = project.properties["compose_version"] as String
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    packaging {
        jniLibs.pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/x86_64/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/x86/libc++_shared.so")
    }
}

dependencies {
    implementation("androidx.media3:media3-effect:1.4.1")
    val kotlinVersion = project.properties["kotlin_version"] as String
    val hiltVersion = project.properties["hilt_version"] as String
    val roomVersion = project.properties["room_version"] as String
    val cameraxVersion = project.properties["camerax_version"] as String

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //openCV
    implementation("org.opencv:opencv:4.10.0")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("jp.co.cyberagent.android:gpuimage:2.1.0")
    //mediaPipe
    implementation("com.google.mediapipe:tasks-vision:0.20230731")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //cameraX
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-video:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-mlkit-vision:$cameraxVersion")
    implementation("androidx.camera:camera-extensions:$cameraxVersion")
    /*第三方库*/
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.arthenica:ffmpeg-kit-full:6.0-2")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.github.Othershe:CombineBitmap:1.0.5")
    // implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.50'
    // 屏幕适配
    implementation("me.jessyan:autosize:1.1.2")
    // 图片加载
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.picasso:picasso:2.71828")

    // 圆角圆形图片控件 主要是可以带边框
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.alibaba:fastjson:1.2.59")
    // implementation(name: 'BaseRecyclerViewAdapterHelper-3.0.6.1', ext: 'aar')
    implementation(files("libs/BaseRecyclerViewAdapterHelper-3.0.6.1.aar"))
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("co.lujun:androidtagview:1.1.6")
    // 权限
    implementation("com.permissionx.guolindev:permissionx:1.4.0")
    implementation("com.danikula:videocache:2.7.1")
    implementation("com.googlecode.mp4parser:isoparser:1.1.21")
    implementation("pub.devrel:easypermissions:3.0.0")
    implementation("com.gitee.zhang-yanqiang:easypermission:v2.0.12")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.lzy.net:okgo:3.0.4")

    // Compose
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.ui:ui:1.7.4")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.4")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.fragment:fragment-ktx:1.8.4")

    // Room 依赖 (用于本地数据库)
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Android Studio 预览支持
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.4")
    // UI 测试
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.4")
    implementation("com.aiyaapp.aiya:AyEffectSDK:4.3.0")

    implementation(project(":chatkit"))
    implementation(project(":comment"))
    implementation(project(":citypicker"))
}

repositories {
    mavenCentral()
}