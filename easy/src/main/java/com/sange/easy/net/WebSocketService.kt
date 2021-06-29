package com.sange.easy.net

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.collection.ArrayMap
import com.sange.base.util.LogUtils
import com.sange.easy.Easy
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
    private val mTag = "WebSocketService"

    /** 心跳接收器 action */
    private val mActionHeartbeat = "heartbeat"

    /** 计时器间隔 */
    private val mTimeInterval = 10000

    /** 重发时间 */
    private val mResentTime = 10000L
    private var webSocket: WebSocket? = null

    // 其他方式：1、OkHttpClient.Builder().build() 2、ApiFactory.newClient()
    private val mClient by lazy { OkHttpClient() }
    private val mListener: WebSocketListener by lazy { providerListener() }
    var mStatus: ConnectStatus? = null

    private val mPendingIntent by lazy { buildPending() }
    private val mAlarmManager by lazy {
        Easy.getAppContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    /** 消息队列 key是服务端返回的event标识，value是消息体,消息不重复存储 */
    private val mMsgQueue = ArrayMap<String, SocketMessage>()

    // 记录心跳次数，收到心跳回复就置为0，否则为1 下次会重连
    private var heartbeatCount = 0

    // 运行计数，10s执行一次，每18次（180s）发一次心跳
    private var count = 0

    // 10s执行一次，每18次（180s）发一次心跳
    private val heartInterval = 18

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
     * 提供WebSocketReceiver继承类
     */
    abstract fun providerReceiver(): Class<*>

    /**
     * 建立连接
     */
    fun connect() = launch {
        LogUtils.v("建立Socket连接")
        close()
        //构造request对象
        val request = Request.Builder()
            .url(providerUrl())
            .build()
        webSocket = mClient.newWebSocket(request, mListener)
        mStatus = ConnectStatus.Connecting
        // 启动心跳
        startHeart()
    }

    /**
     * 关闭Socket连接
     */
    fun close() {
        webSocket?.close(1000, null)
        closeHeart()
    }

    fun cancel() {
        webSocket?.cancel()
    }

    /**
     * 重连
     */
    fun reConnect() {
        LogUtils.e(mTag, "Socket正在重连...")
        webSocket?.let {
            it.close(1000, null)
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
    fun removeMsgCache(event: String) {
        mMsgQueue.remove(event)
    }

    /**
     * （收到心跳回复时调用）重置心跳
     */
    fun resetHeart() {
        heartbeatCount = 0
    }


    /**
     * 构建PendingIntent
     */
    private fun buildPending(): PendingIntent {
        val context = Easy.getAppContext()
        val intent = Intent(context, providerReceiver())
        intent.action = mActionHeartbeat
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    /**
     * 开启心跳
     */
    fun startHeart() {
        mAlarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + mTimeInterval,
            mPendingIntent
        )
    }

    /**
     * 关闭心跳
     */
    fun closeHeart() {
        mAlarmManager.cancel(mPendingIntent)
    }

    /**
     * 心跳任务
     */
    fun onHeartbeatTask() {
        count++
        if (mStatus == ConnectStatus.Connecting) return
        // 是否心跳异常，条件为一个心跳周期内没有收到心跳回复，则为true 心跳异常，false 连接正常
        val isHeartbeatException = count % heartInterval == 0 && heartbeatCount > 0
        // 子类实现的断开连接判定，true 断开连接，false 连接正常
        val isDisconnect = isDisconnect()
        if (isHeartbeatException || isDisconnect) {
            if (isHeartbeatException) {
                LogUtils.e(mTag, "心跳异常，开始重连")
            }
            if (isDisconnect) {
                LogUtils.e(mTag, "子类逻辑Socket异常，开始重连")
            }
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