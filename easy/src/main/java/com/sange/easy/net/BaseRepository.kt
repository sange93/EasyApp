package com.sange.easy.net

import com.sange.easy.Easy
import com.sange.easy.net.ApiFactory.mLoggingInterceptor
import okhttp3.OkHttpClient

/**
 * 网络接口库基类
 *
 * @author ssq
 */
abstract class BaseRepository<T> {
    private val initClient: (OkHttpClient.Builder) -> Unit = { initOkHttpClient(it) }
    /** 网络接口实例 */
    protected val api: T by lazy { ApiFactory.createService( providerBaseUrl(), providerInterface(), initClient) }

    /**
     * 提供网络请求根地址
     * ps:网址必须以"/"结尾
     * e.g. "https://www.你的地址.com/"
     */
    abstract fun providerBaseUrl(): String

    /**
     * 提供Api接口类
     */
    abstract fun providerInterface(): Class<T>

    /**
     * 初始化OkHttp客户端, 重写此方法 可自定义OkHttpClient
     */
    open fun initOkHttpClient(builder: OkHttpClient.Builder){
        if (Easy.isDebugMode()) {// 仅debug模式启用日志过滤器
            builder.addInterceptor(mLoggingInterceptor)
        }
    }
}