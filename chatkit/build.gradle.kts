plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    compileSdk = 29

    defaultConfig {
        minSdk = 14
        consumerProguardFiles("proguard.txt")
        namespace = "com.stfalcon.chatkit"
    }

    lint {
        abortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "prograd-rules.pro")
        }

        getByName("debug") {
            isMinifyEnabled = false // 禁用混淆
            isShrinkResources = false // 禁用资源压缩
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.stfalcon"
                artifactId = "chatkit"
                version = "0.4.1"
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("com.google.android:flexbox:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
}
