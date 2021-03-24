package com.sange.base.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.sange.base.BaseApplication
import com.sange.base.util.UnitUtils

/**
 * 状态栏占位
 * @author ssq
 */
class StatusBarView : View {
    /**
     * 系统状态栏高度（刘海屏高度）
     */
    private val mHeight by lazy { UnitUtils.dp2px(BaseApplication.instance, 20f) }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setMinimumHeight(minHeight: Int) {
        context.applicationContext
        if (minHeight < mHeight) {
            super.setMinimumHeight(mHeight)
        } else {
            super.setMinimumHeight(minHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), mHeight)
    }
}