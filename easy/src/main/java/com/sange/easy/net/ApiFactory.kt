package com.sange.easy.net

import android.os.Build
import com.sange.easy.Easy
import com.sange.easy.json.GsonHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
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
    val mLoggingInterceptor by lazy { HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    } }

    /**
     * 是否开启忽略HTTPS证书
     * 仅适用于暂时还没有HTTPS证书的情况下使用；
     * 默认为false
     */
    var ignoreCertificateEnable = false

    /**
     * 创建API Service接口实例
     */
    fun <T> createService(
        baseUrl: String,
        clazz: Class<T>,
        initOkHttpClient: (builder: OkHttpClient.Builder) -> Unit
    ): T =
        Retrofit.Builder().baseUrl(baseUrl).client(newClient(initOkHttpClient))
            .addConverterFactory(GsonConverterFactory.create(GsonHelper.instance))
            .build().create(clazz)

    /**
     * OkHttpClient客户端
     */
    private fun newClient(initOkHttpClient: (builder: OkHttpClient.Builder) -> Unit): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)// 连接时间：30s超时
            readTimeout(10, TimeUnit.SECONDS)// 读取时间：10s超时
            writeTimeout(10, TimeUnit.SECONDS)// 写入时间：10s超时
            if (ignoreCertificateEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 忽略证书验证 start
                val x509 = X509Manager()
                sslSocketFactory(getSSLFactory(x509), x509)
                hostnameVerifier { _, _ -> true }
                // 忽略证书验证 end
            }

            // 解决Android 4.4及以下的系统默认不支持TLS协议----start
            val trustAllCert = object: X509TrustManager{
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
            val sslSocketFactory = SSLSocketFactoryCompat(trustAllCert)
            sslSocketFactory(sslSocketFactory, trustAllCert)
            // 解决Android 4.4及以下的系统默认不支持TLS协议----end

            initOkHttpClient(this)
        }.build()

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