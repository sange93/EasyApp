package com.sange.easy.net

import androidx.collection.ArrayMap
import com.sange.base.util.LogUtils
import com.sange.easy.net.bean.SocketMessage
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.util.*

/**
 * WebSocket 服务类（全局范围的协程）
 * 负责：连接，心跳机制，
 *
 * @author ssq
 */
abstract class WebSocketService : CoroutineScope by GlobalScope {
    /** 重发时间 */
    private val mResentTime = 10000L
    private var webSocket: WebSocket? = null
    // 其他方式：1、OkHttpClient.Builder().build() 2、ApiFactory.newClient()
    private val mClient by lazy { OkHttpClient() }
    private val mListener: WebSocketListener by lazy { providerListener() }
    var mStatus: ConnectStatus? = null

    /** 心跳计时器 */
    private var mHeartbeatTimer: Timer? = null

    /** 心跳任务 */
    private var mHeartbeatTask: HeartbeatTask? = null

    /** 消息队列 key是服务端返回的event标识，value是消息体,消息不重复存储 */
    private val mMsgQueue = ArrayMap<String, SocketMessage>()

    /**
     * 提供WebSocket服务地址
     * e.g. "wss://你的地址:端口号"
     */
    abstract fun providerUrl(): String

    /**
     * 提供socket监听器
     */
    abstract fun providerListener(): SocketListener

    /**
     * 提供心跳请求数据包
     */
    abstract fun providerHeartbeat(): JSONObject

    /**
     * 建立连接
     */
    fun connect() = launch {
        close()
        //构造request对象
        val request = Request.Builder()
            .url(providerUrl())
            .build()
        webSocket = mClient.newWebSocket(request, mListener)
        mStatus = ConnectStatus.Connecting
        // 启动心跳
        startHeartbeat()
    }

    /**
     * 关闭Socket连接
     */
    fun close() {
        webSocket?.close(1000, null)
        stopHeartbeat()
    }

    fun cancel() {
        webSocket?.cancel()
    }

    /**
     * 重连
     */
    fun reConnect() {
        webSocket?.let {
            mStatus = ConnectStatus.Connecting
            webSocket = mClient.newWebSocket(it.request(), mListener)
        }
    }

    /**
     * 发送消息
     *
     * @param json 未加密的请求数据
     * @param event 消息事件 可见SocketMessenger类中的EVENT常量，如果为null表示不监听无回复重发
     */
    fun send(json: JSONObject, event: String? = null) = launch {
        send(AesUtils.getAesParamOfSocket(json), event)
    }

    /**
     * 发送消息
     *
     * @param
     */
    private suspend fun send(text: String, event: String? = null) = withContext(Dispatchers.IO) {
        event?.let {
            // 将消息记录在队列中，等收到回复，就移除队列
            // 即使断网中，也可以记录消息，连上后再重发
            if (!mMsgQueue.contains(text)) {
                mMsgQueue[it] = SocketMessage(text)
            }
        }
        // 未连接就不发送消息
        /*TODO if (mStatus != ConnectStatus.Open && mStatus != ConnectStatus.Bind) {
            return@withContext
        }*/
        if (mStatus != ConnectStatus.Open && isDisconnect()) {
            return@withContext
        }
        webSocket?.let {
            LogUtils.v("send： $text")
            it.send(text)
        }
    }

    /**
     * 判断是否已断开连接
     * ps:可重写，自定义判断逻辑
     */
    protected open fun isDisconnect(): Boolean = false

    /**
     * 重发消息
     */
    fun reSend() = launch {
        val time = System.currentTimeMillis()
        mMsgQueue.forEach {
            // 判断消息发送超时
            if (time - it.value.sendTime > mResentTime) {
                send(it.value.msg)
            }
        }
    }

    /**
     * 移除发送记录
     */
    fun removeMsgCache(event: String){
        mMsgQueue.remove(event)
    }

    /**
     * 启动心跳
     */
    private fun startHeartbeat() {
        if (mHeartbeatTimer == null) {
            mHeartbeatTimer = Timer()
        }
        if (mHeartbeatTask == null) {
            mHeartbeatTask = HeartbeatTask()
        }
        try {
            mHeartbeatTimer?.schedule(mHeartbeatTask, 0, 10000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 停止心跳
     */
    private fun stopHeartbeat() {
        mHeartbeatTimer?.cancel()
        mHeartbeatTimer = null
        mHeartbeatTask?.cancel()
        mHeartbeatTask = null
    }

    /**
     * （收到心跳回复时调用）重置心跳
     */
    fun resetHeart(){
        mHeartbeatTask?.reset()
    }

    /**
     * 心跳任务
     */
    inner class HeartbeatTask : TimerTask() {
        // 记录心跳次数，收到心跳回复就置为0，否则为1 下次会重连
        private var heartbeatCount = 0

        // 运行计数，10s执行一次，每18次（180s）发一次心跳
        private var count = 0

        // 10s执行一次，每18次（180s）发一次心跳
        private val heartInterval = 18

        /**
         * （收到心跳回复时调用）重置心跳
         */
        fun reset() {
            heartbeatCount = 0
        }

        override fun run() {
            count++
            if (mStatus == ConnectStatus.Connecting) return
//TODO            if ((count % heartInterval == 0 && heartbeatCount > 0) || mStatus != ConnectStatus.Bind) {
            if ((count % heartInterval == 0 && heartbeatCount > 0) || isDisconnect()) {
                count = 0
                // 不为0  代表上次没有收到心跳回复，就执行重连
                reConnect()
                return
            } else if (count % heartInterval == 0) {
                count = 0
                // 发送心跳
                heartbeatCount++
                send(providerHeartbeat())
            }
            // 每10s 检测一遍 有没有需要重发的消息
            reSend()
        }
    }
}