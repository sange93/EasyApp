package com.sange.easy.storageLite

/**
 * 轻量级存储工具类
 *
 * @author ssq
 */
object StorageUtil {
    private lateinit var mDataFileName: String
    // 更换第三方框架时需要修改实现类
    private val instance: IStorage by lazy { SpImpl(mDataFileName) }// MmkvImpl()

    fun instance(dataFileName: String = "custom_app_data"): IStorage{
        mDataFileName = dataFileName
        return instance
    }
}
