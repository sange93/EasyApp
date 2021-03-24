package com.sange.base.weight

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageButton

/**
 * 按下连续执行，抬起停止的按钮
 *
 * @author ssq
 */
class ContinuationImageButton : AppCompatImageButton {
    private var listener: OnClickListener? = null

    constructor(context: Context) :
            super(context)

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> listener?.onDown()
            MotionEvent.ACTION_UP -> listener?.onUp()
        }
        return true
    }

    /**
     * 设置点击事件
     *
     * @param blockOnDown 按下执行的函数
     * @param blockOnUp 抬起执行的函数
     */
    fun setOnClick(blockOnDown: () -> Unit, blockOnUp: () -> Unit) {
        listener = object : OnClickListener {
            override fun onDown() {
                blockOnDown()
            }

            override fun onUp() {
                blockOnUp()
            }
        }
    }

    interface OnClickListener {
        /**
         * 当按下
         */
        fun onDown()

        /**
         * 抬起
         */
        fun onUp()
    }
}