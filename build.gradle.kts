plugins {
    // Android Gradle plugin and Android Studio 对应关系: https://developer.android.google.cn/studio/releases/
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    // kotlin编译器与kotlin版本对应关系：https://developer.android.google.cn/jetpack/androidx/releases/compose-kotlin#kts
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    // 声明 KSP 插件。请务必选择与项目的 Kotlin 版本一致的 KSP 版本。https://developer.android.google.cn/studio/build/migrate-to-ksp?hl=zh-cn
    alias(libs.plugins.ksp) apply false
}