package com.sange.base.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * 时间日期工具类
 *
 * @author ssq
 */
object TimeUtils {
    /**
     * 默认日期格式
     */
    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"

    /**
     * 把当前时间格式化成yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    fun date(): String {
        return SimpleDateFormat(DEFAULT_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
    }

    /**
     * 将时间戳转为时间字符串
     * Milliseconds to the formatted time string.
     *
     * @param millis The milliseconds.
     * @param format The format.
     * @return the formatted time string
     */
    fun millis2String(millis: Long, format: DateFormat): String {
        return format.format(Date(millis))
    }

    /**
     * 获取时间戳(秒)
     * 例：1605864463
     */
    fun getTimestamp(): String = (System.currentTimeMillis() / 1000).toString()
}