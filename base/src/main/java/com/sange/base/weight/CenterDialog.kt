package com.sange.base.weight

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.sange.base.R
import com.sange.base.util.UnitUtils

/**
 * 中间弹窗（配合DialogFragment使用）
 * @author ssq
 * 使用方法：
 * 1、重写方法onCreateDialog(savedInstanceState: Bundle?)
 * 2、返回dialog实例：CenterDialog(activity)
 *
 * @param cancelable 是否点击以外区域可取消
 */
class CenterDialog(context: Context, cancelable: Boolean = false) :
    Dialog(context, R.style.ActionSheetDialogStyle) {

    init {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        context.setTheme(R.style.ActionSheetDialogStyle)
        //消除边距
        window?.decorView?.setPadding(30, 0, 30, 0)
        //设置弹出窗口大小
        val wlp = window?.attributes
        wlp?.width = UnitUtils.dp2px(context, 400F)//WindowManager.LayoutParams.WRAP_CONTENT
        wlp?.height =
            WindowManager.LayoutParams.WRAP_CONTENT//ConvertUtils.dp2px(161F)//WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = wlp
        setCanceledOnTouchOutside(cancelable)
        setCancelable(cancelable)
    }
}