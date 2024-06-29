package com.sange.easy.eventBus

interface EventListener {
    /**
     * 事件处理回调
     * @param tag 事件标识
     * @param data 携带数据
     */
    fun onEvent(tag: String, data: String)
}