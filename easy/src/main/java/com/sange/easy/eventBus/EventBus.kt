package com.sange.easy.eventBus

import java.lang.ref.WeakReference

/**
 * 简易的事件通知，可用于Compose、Activity等界面
 * <p>
 * 使用方式：
 * 1、在ViewModel类，继承并实现EventListener;
 * 2、在ViewModel类，注册监听
 *     init {
 *         MyEventBus.registerListener(this)
 *     }
 * 3、在ViewModel类，解除注册监听
 *     override fun onCleared() {
 *         MyEventBus.unregisterListener(this)
 *     }
 *
 * @author ssq
 */
internal object EventBus: IEventBus {
    private val mListenerList = arrayListOf<WeakReference<EventListener>>()

    override fun registerListener(listener: EventListener) {
        mListenerList.forEach {
            if (it.get() == listener) return
        }
        mListenerList.add(WeakReference(listener))
    }

    override fun unregisterListener(listener: EventListener) {
        mListenerList.forEach {
            if (it.get() == listener) {
                mListenerList.remove(it)
                return
            }
        }
    }

    fun postEvent(tag: String, data: String) {
        mListenerList.forEach {
            it.get()?.onEvent(tag, data)
        }
    }
}