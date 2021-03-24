package com.sange.base.util.exception

/**
 * 异常处理器接口
 *
 * @author ssq
 */
interface IExceptionHandler {

    /**
     * 处理异常
     */
    fun catchException(e: Throwable)
}