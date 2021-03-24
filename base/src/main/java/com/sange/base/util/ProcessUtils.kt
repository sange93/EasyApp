package com.sange.base.util

import android.app.ActivityManager
import android.content.Context

/**
 * 进程工具类
 *
 * @author ssq
 */
object ProcessUtils {

    /**
     * 获取进程名
     */
    fun getProcessName(cxt: Context, pid: Int): String {
        //获取ActivityManager对象
        val am = cxt.getSystemService(Context.ACTIVITY_SERVICE)
        if (am is ActivityManager) {
            //在运行的进程
            val runningApps = am.runningAppProcesses
            for (processInfo in runningApps) {
                if (processInfo.pid == pid) {
                    return processInfo.processName
                }
            }
        }
        return ""
    }
}