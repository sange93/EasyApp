package com.sange.base.util

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * 编码工具类
 *
 * @author ssq
 */
object EncodeUtils {

    /**
     * Base64 编码
     */
    fun base64Encode(data: ByteArray): ByteArray = Base64.encode(data, Base64.NO_WRAP)

    /**
     * Base64 解码
     */
    fun base64Decode(data: ByteArray): ByteArray = Base64.decode(data, Base64.NO_WRAP)

    /**
     * URL 编码
     */
    fun urlEncode(data: ByteArray): String = urlEncode(String(data))

    /**
     * URL 编码
     */
    fun urlEncode(data: String): String = URLEncoder.encode(data, Charsets.UTF_8.name())

    /**
     * URL 解码
     */
    fun urlDecode(data: String): String = URLDecoder.decode(data, Charsets.UTF_8.name())
}