package com.sange.easy.net

import android.os.Build
import com.sange.base.util.LogUtils
import com.sange.easy.Easy
import com.sange.easy.json.GsonHelper
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * 接口请求工厂
 * @author ssq
 */
object ApiFactory {
    // 日志拦截器
    private val mLoggingInterceptor: Interceptor by lazy { LoggingInterceptor() }

    /**
     * 是否为调试接口返回的数据模式
     * 警告：“接口调试模式”已开启，body数据将无法继续传递！！！
     * 默认为false
     */
    var debugResponseEnable = false

    /**
     * 是否开启忽略HTTPS证书
     * 仅适用于暂时还没有HTTPS证书的情况下使用；
     * 默认为false
     */
    var ignoreCertificateEnable = false

    /**
     * 创建API Service接口实例
     */
    fun <T> createService(baseUrl: String, clazz: Class<T>, initOkHttpClient: (builder: OkHttpClient.Builder) -> Unit): T =
        Retrofit.Builder().baseUrl(baseUrl).client(newClient(initOkHttpClient))
            .addConverterFactory(GsonConverterFactory.create(GsonHelper.instance))
            .build().create(clazz)

    /**
     * OkHttpClient客户端
     */
    private fun newClient(initOkHttpClient: (builder: OkHttpClient.Builder) -> Unit): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)// 连接时间：30s超时
        readTimeout(10, TimeUnit.SECONDS)// 读取时间：10s超时
        writeTimeout(10, TimeUnit.SECONDS)// 写入时间：10s超时
        if (Easy.isDebugMode()) addInterceptor(mLoggingInterceptor)// 仅debug模式启用日志过滤器
        if(ignoreCertificateEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            // 忽略证书验证 start
            val x509 = X509Manager()
            sslSocketFactory(getSSLFactory(x509), x509)
            hostnameVerifier { _, _ -> true }
            // 忽略证书验证 end
        }
        initOkHttpClient(this)
    }.build()

    /**
     * 日志拦截器
     */
    private class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = StringBuilder()
            val startTime = System.nanoTime()
            val response: Response = with(chain.request()) {
                builder.append(method + "\n")
                builder.append("Sending request\n$url")
                if (method == "POST") {
                    builder.append("?")
                    when (val body = body) {
                        is FormBody -> {
                            for (j in 0 until body.size) {
                                builder.append(body.name(j) + "=" + body.value(j))
                                if (j != body.size - 1) {
                                    builder.append("&")
                                }
                            }
                        }
//                        is MultipartBody -> {}
                    }
                }
                builder.append("\n").append(headers)
                LogUtils.v(builder.toString())
                chain.proceed(this)
            }
            builder.clear()
            builder.append("Received response in " + (System.nanoTime() - startTime) / 1e6 + "ms\n")
            // 下面两行仅是调试接口返回的数据
            if (debugResponseEnable) {
                val res = response.body?.string() ?: ""
                LogUtils.e("警告：“接口调试模式”已开启，body数据将无法继续传递！数据返回：$res")
            }
            builder.append("code" + response.code + "\n")
            LogUtils.v(builder.toString())
            return response
        }
    }

    /**
     * 获取SSL工厂
     */
    private fun getSSLFactory(x509TrustManager: X509TrustManager): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(x509TrustManager)
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        return sslContext.socketFactory
    }
}