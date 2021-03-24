package com.sange.base.util.exception

import android.accounts.NetworkErrorException
import androidx.annotation.StringRes
import com.sange.base.BaseApplication
import com.sange.base.R
import com.sange.base.util.ToastUtils
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 异常处理器基类
 * 自定义处理器可继承此类
 *
 * @author ssq
 */
open class BaseExceptionHandler : IExceptionHandler {

    override fun catchException(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is SocketTimeoutException -> showToast(
                R.string.base_error_net_time_out
            )
            is UnknownHostException, is NetworkErrorException -> showToast(
                R.string.base_error_net
            )
        }
        if (!onException(e)) {
            showUnknown(e)
        }
    }

    /**
     * 子类处理异常
     *
     * @return true 异常已处理 false 未处理
     */
    protected open fun onException(e: Throwable): Boolean {
        return false
    }

    /**
     * 处理网络异常
     */
    protected fun catchHttpException(errorCode: Int) {
        if (errorCode in 200 until 300) return// 成功code则不处理
        showToast(
            catchHttpExceptionCode(
                errorCode
            ), errorCode
        )
    }

    /**
     * 处理网络异常
     */
    private fun catchHttpExceptionCode(errorCode: Int): Int = when (errorCode) {
        in 500..600 -> R.string.base_error_server
        in 400 until 500 -> R.string.base_error_request
        else -> R.string.base_error_request
    }

    /**
     * 显示未知异常
     */
    private fun showUnknown(e: Throwable) {
        showToast(
            "${
                BaseApplication.instance.getString(
                    R.string.base_error_do_something_fail
                )
            }：${e::class.java.name}"
        )
    }

    /**
     * toast提示
     */
    protected fun showToast(@StringRes errorMsg: Int, errorCode: Int = -1) {
        showToast(
            BaseApplication.instance.getString(
                errorMsg
            ), errorCode
        )
    }

    /**
     * toast提示
     */
    protected fun showToast(errorMsg: String, errorCode: Int = -1) {
        if (errorCode == -1) {
            ToastUtils.showShort(errorMsg)
        } else {
            ToastUtils.showShort("$errorCode：$errorMsg")
        }
    }
}