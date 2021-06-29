package com.sange.easy.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

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
            with(providerService()) {
                startHeart()
                onHeartbeatTask()
            }
        }
    }

    /**
     * 提供WebSocketService 实例
     */
    abstract fun providerService(): WebSocketService
}