package com.sange.easy.net

/**
 * 网络接口库基类
 *
 * @author ssq
 */
abstract class BaseRepository<T> {
    /** 网络接口实例 */
    protected val api: T by lazy { ApiFactory.createService( providerBaseUrl(), providerInterface()) }

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
}