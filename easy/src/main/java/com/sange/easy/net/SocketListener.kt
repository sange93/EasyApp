package com.sange.easy.net

import com.sange.base.util.LogUtils
import com.sange.base.util.ToastUtils
import com.sange.base.util.getStringRes
import com.sange.easy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.lang.ref.WeakReference

/**
 * socket监听器
 *
 * @author ssq
 */
open class SocketListener(socketService: WebSocketService) : WebSocketListener() {
    private val mSocketService = WeakReference(socketService)

    /**
     * WebSocket 连接建立
     */
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        LogUtils.v("onOpen")
        mSocketService.get()?.mStatus = ConnectStatus.Open
    }

    /**
     * 收到String消息
     */
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        LogUtils.v("onMessage(String): $text")
    }

    /**
     * 收到服务端发来的 CLOSE 帧消息，准备关闭连接
     */
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        LogUtils.v("onClosing")
        mSocketService.get()?.mStatus = ConnectStatus.Closing
    }

    /**
     * WebSocket 连接关闭
     */
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        LogUtils.v("onClosed")
        mSocketService.get()?.mStatus = ConnectStatus.Closed
    }

    /**
     * 出错了
     */
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        LogUtils.v("onFailure: $t")
        t.printStackTrace()
        mSocketService.get()?.mStatus = ConnectStatus.Canceled
        showFailure()
    }
    /**
     * 显示网络异常提示
     */
    private fun showFailure() = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            ToastUtils.showShort(R.string.base_error_net.getStringRes())
        }
    }
}