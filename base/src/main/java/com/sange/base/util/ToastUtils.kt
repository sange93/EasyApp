package com.sange.base.util

import android.widget.Toast
import com.sange.base.BaseApplication

/**
 * Toast提示工具类
 *
 * @author ssq
 */
object ToastUtils {

    fun showShort(msg: String){
        Toast.makeText(BaseApplication.instance,msg,Toast.LENGTH_SHORT).show()
    }

    fun showLong(msg: String){
        Toast.makeText(BaseApplication.instance,msg,Toast.LENGTH_LONG).show()
    }
}