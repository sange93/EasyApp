package com.sange.easy.net

import com.sange.base.R
import com.sange.base.util.EncodeUtils.base64Decode
import com.sange.base.util.EncodeUtils.base64Encode
import com.sange.base.util.EncodeUtils.urlDecode
import com.sange.base.util.EncodeUtils.urlEncode
import com.sange.base.util.LogUtils
import com.sange.base.util.and
import com.sange.base.util.getStringRes
import org.json.JSONObject
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES128 算法
 * CBC 模式
 * PKCS7Padding 填充模式
 * CBC模式需要添加一个参数iv
 * 介于java 不支持PKCS7Padding，只支持PKCS5Padding
 * 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
 *
 * @author ssq
 */
object AesUtils {
    private const val TAG = "AesUtil"
    const val keyEncrypt = "encrypt"
    const val keyIV = "iv"

    // Socket、Http 私钥
    private lateinit var mSecret: String

    // Socket 签名的私钥
    private lateinit var mSocketSignSecret: String

    // Http 签名的私钥
    private lateinit var mHttpSignSecret: String

    // 加解密算法/模式/填充方式 // "AES/CBC/PKCS7Padding", "BC"
    private val mCipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    // 转化成JAVA的密钥格式
    private val mKey by lazy { SecretKeySpec(getSecret().toByteArray(), "AES") }

    /**
     * 初始化
     * 必须在使用前调用,
     * 建议在Application中调用
     *
     * @param secret 私钥
     * @param socketSignSecret Socket 签名的私钥
     * @param httpSignSecret Http 签名的私钥
     */
    fun init(secret: String,socketSignSecret: String, httpSignSecret: String){
        if(!this::mSecret.isInitialized){
            mSecret = secret
        }
        if(!this::mSocketSignSecret.isInitialized){
            mSocketSignSecret = socketSignSecret
        }
        if(!this::mHttpSignSecret.isInitialized){
            mHttpSignSecret = httpSignSecret
        }
    }

    /**
     * 获取私钥
     */
    private fun getSecret() = if(this::mSecret.isInitialized) mSecret else notInit()

    /**
     * 获取Socket 签名的私钥
     */
    private fun getSocketSignSecret() = if(this::mSocketSignSecret.isInitialized) mSocketSignSecret else notInit()

    /**
     * 获取Http 签名的私钥
     */
    private fun getHttpSignSecret() = if(this::mHttpSignSecret.isInitialized) mHttpSignSecret else notInit()

    /**
     * 未执行init()初始化
     */
    private fun notInit(): String{
        LogUtils.e(TAG, R.string.base_error_not_call_aes_init.getStringRes())
        throw NotImplementedError()
    }

    /**
     * 加密
     */
    private fun encrypt(content: ByteArray, iv: String): ByteArray {
        val ivParameterSpec = IvParameterSpec(iv.substring(0, 16).toByteArray())
        mCipher.init(Cipher.ENCRYPT_MODE, mKey, ivParameterSpec)
        return mCipher.doFinal(content)
    }

    /**
     * 获取加密后的请求参数
     */
    fun getAesParamOfHttp(param: JSONObject): JSONObject {
        val sign = buildSign(param, getHttpSignSecret())
        return getAesParam(param, sign)
    }

    /**
     * 加密(Socket)
     */
    fun getAesParamOfSocket(param: JSONObject): String {
        val sign = buildSign(param, getSocketSignSecret())
        return getAesParam(param, sign).toString()
    }

    /**
     * 加密
     *
     * @param param 待加密的参数，例：{"name":"jack","time":1605864463,"msg":"hello"}
     * @param sign 签名
     * @return 加密后，要发送的数据。例：{"encrypt":"lfH%2FmNFdfUXShvKUVuHDXiTIqHIQ","iv":"pzRFTOQ%3D%0A"}
     */
    private fun getAesParam(param: JSONObject, sign: String): JSONObject {
        param.put("sign", sign)
        val function = param.optString("function")
        LogUtils.i(TAG, "------ 发送:$function")
        LogUtils.v("加密前:$param")
        val iv = sign.substring(0, 16)

        val param64 = base64Encode(param.toString().toByteArray())
        val dataBase = encrypt(param64, iv)

        val encrypted = urlEncode(base64Encode(dataBase))
        val ivString = urlEncode(base64Encode(sign.toByteArray()))

        return JSONObject().apply {
            put(keyEncrypt, encrypted)
            put(keyIV, ivString)
        }
    }

    /**
     * 解密
     *
     * @param jsonString 待解密的json数据
     */
    fun getDecodeContent(jsonString: String): String {
        val json = JSONObject(jsonString)
        val encrypt = json.getString(keyEncrypt)
        val iv = json.getString(keyIV)
        return getDecodeContent(encrypt, iv)
    }

    /**
     * 解密
     *
     * @param data 待解密的数据
     * @param iv 偏移量
     */
    private fun getDecodeContent(data: String, iv: String): String =
        try {
            val ivUrl = urlDecode(iv)
            val dataUrl = urlDecode(data)
            val ivString = String(base64Decode(ivUrl.toByteArray()))
            val data64 = base64Decode(dataUrl.toByteArray())
            val dataCrypt = decrypt(data64, ivString.substring(0, 16))
            String(base64Decode(dataCrypt))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     */
    private fun decrypt(encryptedData: ByteArray, iv: String): ByteArray {
        val ivParameterSpec = IvParameterSpec(iv.substring(0, 16).toByteArray())
        mCipher.init(Cipher.DECRYPT_MODE, mKey, ivParameterSpec)
        return mCipher.doFinal(encryptedData)
    }

    /**
     * 根据参数生成签名
     *
     * @param secret 秘钥 1、mHttpSignSecret 2、mSocketSignSecret
     */
    private fun buildSign(json: JSONObject, secret: String): String {
        // 按字典排序
        val keys = arrayListOf<String>()
        json.keys().forEach {
            keys.add(it)
        }
        keys.sort()// 按首字母升序排
        val stringBuilder = StringBuilder()
        keys.forEach {
            val value = json.getString(it)
            if (value.isEmpty()) {
                LogUtils.e(TAG, "key: $it ------value 不能为空")
            }
            stringBuilder.append(it).append("=").append(value).append("&")
        }
        // 拼接私钥
        stringBuilder.append("secret=$secret")

        var sign = ""
        try {
            val sha256HMAC = Mac.getInstance("HmacSHA256")
            sha256HMAC.init(SecretKeySpec(secret.toByteArray(), "HmacSHA256"))
            val hash = sha256HMAC.doFinal(stringBuilder.toString().toByteArray())
            sign = byteArrayToHexString(hash)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sign
    }

    /**
     * byte数组转 十六进制字符串
     */
    private fun byteArrayToHexString(b: ByteArray?): String {
        val sb = java.lang.StringBuilder()
        var tmp: String
        var n = 0
        while (b != null && n < b.size) {
            tmp = Integer.toHexString(b[n] and 0XFF)
            if (tmp.length == 1) sb.append('0')
            sb.append(tmp)
            n++
        }
        return sb.toString().toLowerCase(Locale.getDefault())
    }
}