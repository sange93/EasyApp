import com.android.build.api.dsl.LibraryExtension
plugins {
    alias(libs.plugins.android.library)
//    id("kotlin-kapt")
    alias(libs.plugins.ksp)
    `maven-publish`
    alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension> {
    namespace = "com.sange.easy"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //-----------以下为定制内容------------
    // 基础库
    api(libs.baseapp)
    // 图片加载库
    api(libs.glide)
//    kapt("com.github.bumptech.glide:compiler:4.16.0") 迁移ksp
    ksp(libs.glide.ksp)
    // Gson Json 解析框架：https://github.com/google/gson
    implementation(libs.gson)
    // Gson 解析容错：https://github.com/getActivity/GsonFactory
    implementation(libs.gsonfactory)
    // EventBus 线程间通讯 观察者模式
    api(libs.eventbus)
    // Retrofit 网络请求 https://github.com/square/retrofit
    api(libs.retrofit)
    api(libs.retrofit.converter.gson)
    // OkHttp https://github.com/square/okhttp
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)
    // kotlin json 序列化
    api(libs.kotlinx.serialization.json)

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
                version = "1.4.0"
//                from(components["release"]) Android Library 项目，组件名是 android，不是 release
                from(components.findByName("android"))
            }
        }
    }
}