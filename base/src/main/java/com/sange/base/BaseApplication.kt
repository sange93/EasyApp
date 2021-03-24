package com.sange.base

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.sange.base.util.ProcessUtils


/**
 * 基础Application
 *
 * @author ssq
 */
abstract class BaseApplication : MultiDexApplication() {
    companion object {
        // app实例
        lateinit var instance: BaseApplication

        // 是否为debug模式
        var isDebugMode: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //处理Application的onCreate多次调用问题,通过进程的名称来区分执行哪些具体逻辑。
        if (getAppPackageName() == ProcessUtils.getProcessName(this, android.os.Process.myPid())) {
            initApp()
            // TODO 延迟方法  待延迟实现
            delayInitApp()
        }
    }

    /**
     * 获取APP包名
     */
    abstract fun getAppPackageName(): String

    /**
     * 做一些初始化
     */
    protected open fun initApp() {
        //debug版本赋值，比Build.DEBUG好用
        initDebug()
    }

    /**
     * 一些延迟的初始化
     * APP启动优化：将一些不必要的初始化放在子线程延迟初始化，可提高应用的启动速度
     */
    protected open fun delayInitApp() {
    }

    /**
     * 获取是否debug版本
     */
    private fun initDebug(){
        isDebugMode = instance.applicationInfo != null && instance.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }
}