package com.sange.easy

import android.content.Context
import com.sange.base.BaseApplication
import com.sange.base.util.LogUtils
import com.sange.base.util.exception.ExceptionUtil
import com.sange.base.util.getStringRes
import com.sange.easy.imageLoader.ImageLoaderUtil
import com.sange.easy.json.JsonUtil
import com.sange.easy.storageLite.IStorage
import com.sange.easy.storageLite.StorageUtil

/**
 * 易工具
 *
 * @author ssq
 */
object Easy {
    private const val TAG = "Easy"
    private lateinit var mAppContext: Context

    /** 是否为调试模式 */
    private var mIsDebugMode = false

    /**
     * 初始化
     * ps：必须在app模块的Application中调用
     *
     * @param exceptionHandler 异常处理器，默认为EasyExceptionHandler
     */
    fun init(appContext: Context, exceptionHandler: EasyExceptionHandler = EasyExceptionHandler()) {
        mAppContext = appContext
        mIsDebugMode = BaseApplication.isDebugMode
        ExceptionUtil.init(exceptionHandler)
    }

    fun getAppContext(): Context =
        if (this::mAppContext.isInitialized) {
            mAppContext
        } else {
            LogUtils.e(TAG, R.string.easy_error_not_call_easy_init.getStringRes())
            throw NotImplementedError()
        }

    /**
     * 是否为调试模式
     */
    fun isDebugMode() = mIsDebugMode

    /**
     * 轻量级存储工具类
     *
     * @param dataFileName 数据文件名
     */
    fun getStorageUtil(dataFileName: String? = null): IStorage {
        return if(dataFileName == null){
            StorageUtil.instance()
        }else{
            StorageUtil.instance(dataFileName)
        }
    }

    /**
     * 图片加载 工具类
     *
     * @param any 可以是view、activity、fragment、context。
     */
    fun getImageLoaderUtil(any: Any) = ImageLoaderUtil.with(any)

    /**
     * Json解析处理 工具类
     */
    fun getJsonUtil() = JsonUtil.impl()
}