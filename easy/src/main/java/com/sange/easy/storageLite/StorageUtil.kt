package com.sange.easy.storageLite

/**
 * 轻量级存储工具类
 *
 * @author ssq
 */
object StorageUtil {
    // 更换第三方框架时需要修改实现类
    private val instance: IStorage by lazy { SpImpl() }// MmkvImpl()

    fun instance(): IStorage = instance
}
