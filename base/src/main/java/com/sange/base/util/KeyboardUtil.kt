package com.sange.base.util

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.sange.base.BaseApplication

/**
 * 键盘工具类
 *
 * @author ssq
 */
object KeyboardUtil {

    /**
     * 点击空白区域隐藏键盘
     * 在界面的dispatchTouchEvent(ev: MotionEvent?)方法中调用
     */
    fun clickAreaHideKeyboard(ev: MotionEvent?, activity: FragmentActivity) {
        ev?.let {
            if (it.action == MotionEvent.ACTION_DOWN) {
                val v = activity.currentFocus
                if (v != null && isShouldHideKeyboard(v, ev)) {
                    val imm: InputMethodManager =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }
    }

    private fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
        if (v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    /**
     * 显示软键盘
     */
    fun showSoftInput(view: View) {
        val imm = BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            imm.showSoftInput(view, 0, object : ResultReceiver(Handler(Looper.getMainLooper())) {

                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN
                    ) {
                        toggleSoftInput()
                    }
                }
            })
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    /**
     * 开关软键盘显示
     */
    private fun toggleSoftInput() {
        val imm = BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE)
        if(imm is InputMethodManager) {
            imm.toggleSoftInput(0, 0)
        }
    }
}