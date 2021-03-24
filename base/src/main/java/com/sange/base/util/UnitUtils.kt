package com.sange.base.util

import android.content.Context

/**
 * 单位工具类
 *
 * @author ssq
 */
object UnitUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dp: Float) =
        (dp * context.resources.displayMetrics.density + 0.5f).toInt()

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, px: Float) =
        (px / context.resources.displayMetrics.density + 0.5f).toInt()
}