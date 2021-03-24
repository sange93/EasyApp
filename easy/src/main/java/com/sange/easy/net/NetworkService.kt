package com.sange.easy.net

import java.lang.reflect.TypeVariable

/**
 * 网络服务类
 * @author ssq
 */
object NetworkService {
    //-------------测试环境------------
    // 请求根地址
//    private const val BASE_URL = "http://api.wanke.test.wx.xz.cn/"
//    // WebSocket服务地址
//    const val WS_URL = "wss://test.merchant.zhanghutong.cn:9501"
    //-------------正式环境------------
    // 请求根地址
//    private const val BASE_URL = "https://api.wanke.wx.xz.cn/"
    // WebSocket服务地址
//    const val WS_URL = "wss://api.wanke.wx.xz.cn:9501"

//    // 请求根地址
//    private var BASE_URL = ""
//    // Api接口类
//    private var mApiInterfaceClazz: Class<out Any> = ApiService::class.java

    // 接口API服务(挂起)
//    val api by lazy { ApiFactory.createService(BASE_URL, mApiInterfaceClazz) }
//    val api by lazy { ApiFactory.createService(BASE_URL, ApiService::class.java) }
//    lateinit var api: Any

    /*
     * 初始化Api
     * 必须在api接口执行前调用此方法
     * ps: 只可调用一次
     *
     * @param baseUrl 请求根地址
     * @param apiClazz Api接口类
     */
    /*fun initApi(baseUrl: String, apiClazz: Class<out Any>){
        if(!this::api.isInitialized) {
            BASE_URL = baseUrl
            mApiInterfaceClazz = apiClazz
            api = ApiFactory.createService(baseUrl, apiClazz)
        }
    }*/
}