package com.sange.easy.net.bean

/**
 * Socket消息
 *
 * @param msg 消息内容
 * @param sendTime 发送时间
 *
 * @author ssq
 */
class SocketMessage(val msg: String, val sendTime: Long = System.currentTimeMillis())