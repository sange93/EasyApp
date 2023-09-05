buildscript {
    /*ext.kotlin_version = "1.8.0"
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }*/
}
plugins {
    // Android Gradle plugin and Android Studio 对应关系: https://developer.android.google.cn/studio/releases/
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    // kotlin编译器与kotlin版本对应关系：https://developer.android.google.cn/jetpack/androidx/releases/compose-kotlin#kts
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false// kotlin插件版本
    // 声明 KSP 插件。请务必选择与项目的 Kotlin 版本一致的 KSP 版本。https://developer.android.google.cn/studio/build/migrate-to-ksp?hl=zh-cn
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}