package com.sange.easy.net.bean

import com.sange.easy.Easy

/**
 * http接口返回的加密数据
 *
 * @author ssq
 */
class HttpData {
    var code = -1// 200 成功
    var message = ""
    var data = ""
    var decodeJson = ""// 解密后的数据json（本地属性）

    /**
     * 获取接收到的加密数据
     */
    suspend fun getEncryptData() = Easy.getJsonUtil().fromJson(data,EncryptData::class.java)

    /**
     * 获取集合类型的数据
     *
     * @param clazz 集合内的数据类型
     */
    fun <T> getListData(clazz: Class<T>): List<T> {
        return if (decodeJson.isNotEmpty()) {
            Easy.getJsonUtil().fromJson2List(decodeJson, clazz)
        } else {
            ArrayList()
        }
    }

    /**
     * 获取对象类型的数据
     *
     * @param clazz 数据类型
     */
    fun <T> getObjectData(clazz: Class<T>): T =
        if (decodeJson.isNotEmpty()) {
            Easy.getJsonUtil().fromJson(decodeJson, clazz)
        } else {
            clazz.getConstructor().newInstance()
        }

}

/**
 * 加密数据
 *
 * @author ssq
 */
class EncryptData {
    var encrypt = ""
    var iv = ""
}