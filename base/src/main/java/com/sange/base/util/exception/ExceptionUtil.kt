package com.sange.base.util.exception

/**
 * 异常工具类
 * 自定义异常处理器 请调用init()方法。
 *
 * @author ssq
 */
object ExceptionUtil {

    /** 异常处理器 */
    private lateinit var mExceptionHandler: IExceptionHandler

    /**
     * 初始化（自定义）异常处理器
     * ps: 只可调用一次
     */
    fun init(exceptionHandler: BaseExceptionHandler) {
        if (!this::mExceptionHandler.isInitialized) {
            mExceptionHandler = exceptionHandler
        }
    }

    /**
     * 获取异常处理器
     */
    fun getExceptionHandler(): IExceptionHandler {
        if (!this::mExceptionHandler.isInitialized) {
            // 生成默认处理器
            mExceptionHandler = BaseExceptionHandler()
        }
        return mExceptionHandler
    }
}