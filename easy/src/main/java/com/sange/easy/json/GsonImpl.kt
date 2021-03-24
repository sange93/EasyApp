package com.sange.easy.json

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.sange.base.util.getStringRes
import com.sange.easy.R
import com.sange.easy.json.gson.MGson

/**
 * Gson解析
 *
 * @author ssq
 */
class GsonImpl : IJson {

    /**
     * 对象转Json
     *
     * @param src 将要被转的对象
     */
    override fun toJson(src: Any): String {
        try {
            return MGson.getInstance().toJson(src)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw JsonException(t.message ?: R.string.easy_error_to_json.getStringRes())
        }
    }

    /**
     * 对象转Json
     *
     * @param src 将要被转的对象
     * @param skipField 排除的参数
     */
    override fun toJson(src: Any, vararg skipField: String): String {
        try {
            val strategy = object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes?): Boolean {
                    val name = f?.name ?: return false
                    skipField.forEach {
                        if (name == it) return true
                    }
                    return false
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean = false
            }
            val gson = GsonBuilder().setExclusionStrategies(strategy).create()
            return gson.toJson(src)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw JsonException(t.message ?: R.string.easy_error_to_json.getStringRes())
        }
    }

    /**
     * 解析Json
     *
     * @param json 要解析的json
     * @param clazz 转换的数据类型
     */
    override fun <T> fromJson(json: String, clazz: Class<T>): T {
        try {
            return MGson.getInstance().fromJson(json, clazz)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw JsonException(t.message ?: R.string.easy_error_parse_json.getStringRes())
        }
    }

    /**
     * 解析Json串为集合
     *
     * @param json Json字符串
     * @param clazz 类型
     * @param <T> 类型
     * @return 集合
     */
    override fun <T> fromJson2List(json: String, clazz: Class<T>): List<T> {
        try {
            return MGson.parseString2List(json, clazz)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw JsonException(t.message ?: R.string.easy_error_parse_json.getStringRes())
        }
    }
}