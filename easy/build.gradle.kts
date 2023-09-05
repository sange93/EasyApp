plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    `maven-publish`
}

group = "com.github.sange93"
version = "1.2.6"

android {
    namespace = "com.sange.easy"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles "consumer-rules.pro"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //-----------以下为定制内容------------
    // 基础库
    implementation("com.github.sange93:BaseApp:1.0.20")
    // 图片加载库
    api("com.github.bumptech.glide:glide:4.16.0")
//    kapt("com.github.bumptech.glide:compiler:4.16.0") 迁移ksp
    ksp("com.github.bumptech.glide:ksp:4.16.0")
    // Gson Json 解析框架：https://github.com/google/gson
    implementation("com.google.code.gson:gson:2.10.1")
    // Gson 解析容错：https://github.com/getActivity/GsonFactory
    implementation("com.github.getActivity:GsonFactory:6.5")
    // EventBus 线程间通讯 观察者模式
    api("org.greenrobot:eventbus:3.3.1")
    // Retrofit网络请求
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp
    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.4")

    //-----------以下为推荐内容，本组件不包含，可在app模块按需添加------------
    // MMKV——基于 mmap 的高性能通用 key-value 组件,用于替代SharedPreferences
//    implementation 'com.tencent:mmkv-static:1.2.6'
    // leakcanary 内存泄漏分析框架，Logcat会有大量的GC日志，说明有泄露；
//    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.5'
    // 异常上报 腾讯bugly
//    implementation 'com.tencent.bugly:crashreport:4.1.9'
    // js库
//    implementation 'com.github.lzyzsd:jsbridge:1.0.4'
    // 视频缓存
//    implementation 'com.danikula:videocache:2.7.1'
    // 腾讯VAP 动画播放器
//    implementation "com.egame.vap:animplayer:2.0.13"
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release"){
                groupId = "com.github.sange93"
                artifactId = "EasyApp"
                version = "1.2.6"
                from(components["release"])
            }
        }
    }
}