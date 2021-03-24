package com.sange.base.util

import com.sange.base.BaseApplication

/**
 * 将 int 类型数据转成十六进制的字符串，不足 int 类型位数时在前面添“0”以凑足位数
 *
 * @return 8位字符 例：0000004e
 */
fun Int.toFullHex(): String {
    val hex = "0123456789abcdef".toCharArray()
    val chs = CharArray(Integer.SIZE / 4)
    for (i in chs.indices) {
        chs[chs.size - 1 - i] = hex[this shr i * 4 and 0xf]
    }
    return String(chs)
}

/**
 * 如果int是string资源ID，可以使用此函数获取文字String
 */
fun Int.getStringRes(): String = BaseApplication.instance.getString(this)