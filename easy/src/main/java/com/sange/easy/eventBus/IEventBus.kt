package com.sange.easy.eventBus

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
interface IEventBus {
    /**
     * 注册监听
     */
    fun registerListener(listener: EventListener)

    /**
     * 解除注册监听
     */
    fun unregisterListener(listener: EventListener)
}