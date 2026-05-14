package com.sange.easy.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * Json 工具类(支持 Kotlin 数据类的默认参数值)
 */
object JsonUtilKt {
    // 全局统一的 Json 配置（只初始化一次）
    val json = Json {
        ignoreUnknownKeys = true     // 忽略 JSON 多余字段（必开）
        encodeDefaults = true        // 启用 Kotlin 默认值（必开！）
        explicitNulls = false        // 不序列化 null
        prettyPrint = false          // 关闭格式化（生产环境）
        isLenient = true             // 宽松解析
        coerceInputValues = true     // 类型不匹配时自动兼容（如 int→string）
    }

    // 对象 → JSON
    fun <T> toJson(obj: T, serializer: KSerializer<T>): String {
        return json.encodeToString(serializer, obj)
    }

    // 简化版：直接传对象
    inline fun <reified T> toJson(obj: T): String {
        return json.encodeToString(obj)
    }

    // JSON → 对象
    inline fun <reified T> fromJson(jsonStr: String): T {
        return json.decodeFromString(jsonStr)
    }

    // JSON → 列表
    inline fun <reified T> fromJsonList(jsonStr: String): List<T> {
        return json.decodeFromString(jsonStr)
    }
}