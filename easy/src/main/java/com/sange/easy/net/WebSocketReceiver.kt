package com.sange.easy.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sange.base.util.LogUtils

/**
 * Socket连接通知接收器
 * (需继承此类，并在AndroidManifest.xml注册)
 *
 * @author ssq
 */
abstract class WebSocketReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        if (intent.action == "heartbeat") {
            with(providerService()){
                startHeart()
                onHeartbeatTask()
            }
            LogUtils.e("收到BroadcastReceiver：heartbeat")
        }
    }

    /**
     * 提供WebSocketService 实例
     */
    abstract fun providerService(): WebSocketService
}