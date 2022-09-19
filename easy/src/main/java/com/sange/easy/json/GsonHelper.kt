package com.sange.easy.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hjq.gson.factory.GsonFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// 高级用法，可在项目中自行配置
// 设置 Json 解析容错监听
/*GsonFactory.setJsonCallback(new JsonCallback() {

    @Override
    public void onTypeException(TypeToken<?> typeToken, String fieldName, JsonToken jsonToken) {
        // Log.e("GsonFactory", "类型解析异常：" + typeToken + "#" + fieldName + "，后台返回的类型为：" + jsonToken);
        // 上报到 Bugly 错误列表中
        CrashReport.postCatchedException(new IllegalArgumentException("类型解析异常：" + typeToken + "#" + fieldName + "，后台返回的类型为：" + jsonToken));
    }
});*/

/**
 * Gson帮助类（支持容错处理）
 * 解决gson解析类型不对，导致数据crash
 * @author ssq
 */
object GsonHelper {
    /** 获取单例的 Gson 对象（已处理容错）*/
    val instance: Gson = GsonFactory.getSingletonGson()

    /**
     * 创建一个 Gson 构建器（已处理容错）
     */
    fun builder(): GsonBuilder = GsonFactory.newGsonBuilder()

    /**
     * 解析json字符串为集合
     * @param json Json字符串
     * @param clazz 类型
     * @param <T> 类型
     * @return 集合
    </T> */
    fun <T> parseString2List(json: String, clazz: Class<T>): List<T> {
        val type: Type = ParameterizedTypeImpl(clazz)
        var list = instance.fromJson<List<T>>(json, type)
        if (list == null) {
            list = ArrayList()
        }
        return list
    }

    private class ParameterizedTypeImpl<T>(var clazz: Class<T>) : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(clazz)
        }

        override fun getRawType(): Type {
            return MutableList::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }
}