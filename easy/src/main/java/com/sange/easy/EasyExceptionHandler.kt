package com.sange.easy

import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.sange.base.util.LogUtils
import com.sange.base.util.exception.BaseExceptionHandler
import com.sange.easy.net.ApiException
import retrofit2.HttpException

/**
 * 异常处理器
 *
 * 如果自定义异常处理器，请继承此类。
 *
 * @author ssq
 */
open class EasyExceptionHandler : BaseExceptionHandler() {

    override fun onException(e: Throwable): Boolean = when (e) {
        is HttpException -> {
            catchHttpException(e.code())
            true
        }
        is MalformedJsonException, is JsonSyntaxException -> {
            showToast(R.string.easy_error_server_json)
            true
        }
        // 接口异常
        is ApiException -> {
            LogUtils.e("ApiException: ${e.errorCode}  ${e.msg}")
            showToast(e.msg, e.errorCode)
            true
        }
        else -> false
    }
}