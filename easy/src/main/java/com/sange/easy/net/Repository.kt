package com.sange.easy.net

import com.sange.base.util.LogUtils
import com.sange.easy.Easy
import com.sange.easy.net.bean.BaseBean
import com.sange.easy.net.bean.HttpData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * 接口资源
 * @author ssq
 */
abstract class Repository : BaseRepository<ApiService>() {

    override fun providerInterface(): Class<ApiService> = ApiService::class.java

    /**
     * 下载文件
     */
    suspend fun downloadFile(fileUrl: String): ResponseBody = withContext(Dispatchers.IO) {
        api.downloadFileAsync(fileUrl)
    }

    /**
     * 发送请求
     *
     * @param param 请求参数
     * @param clazz 返回的数据类型
     * @return http返回的解密数据
     */
    protected suspend fun <T : BaseBean> request(param: JSONObject, clazz: Class<T>): T {
        // 加密请求参数
        val data = AesUtils.getAesParamOfHttp(param)
        // 发送请求
        val httpData = api.request(getEncrypt(data), getIV(data))
        // 处理数据
        val dataString = Easy.getJsonUtil().toJson(httpData.data)
        if(dataString.contains("encrypt")){
            val encryptData = httpData.getEncryptData()
            if (encryptData.encrypt.isNotEmpty() && encryptData.iv.isNotEmpty()) {
                val httpJson = Easy.getJsonUtil().toJson(encryptData)
                // 解密数据
                httpData.decodeJson = AesUtils.getDecodeContent(httpJson)
            }
        }else{
            httpData.decodeJson = dataString
        }
        LogUtils.v("HTTP解密：${httpData.decodeJson}")
        // 创建结果数据实例
        val result = clazz.getConstructor(HttpData::class.java).newInstance(httpData)
        // 预处理数据 并返回数据结果
        return preData(result)
    }

    /**
     * 预处理数据(错误)
     */
    private fun <T : BaseBean> preData(baseBean: T): T =
        if (baseBean.code == 200) {// 成功
            // 返回数据
            baseBean
        } else {// 失败
            // 抛出接口异常
            throw ApiException(baseBean.code, baseBean.msg)
        }

    /**
     * 获取加密后的请求参数
     */
    private fun getEncrypt(param: JSONObject) = param.getString(AesUtils.keyEncrypt)

    /**
     * 获取偏移量
     */
    private fun getIV(param: JSONObject) = param.getString(AesUtils.keyIV)

    /*
     * 获取用户信息（示例代码）
     *
     * @param param （未加密的）请求参数
     */
//    suspend fun getUser(param: JSONObject) = request(param, UserData::class.java)
}