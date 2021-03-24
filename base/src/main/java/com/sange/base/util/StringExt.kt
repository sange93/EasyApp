package com.sange.base.util

import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern

/**
 * 扩展函数：拼接CRC
 */
fun String.crc(): String {
    // 去除空格 并 转为小写
    val dataTmp = this.replace(" ", "").toLowerCase(Locale.ENGLISH)
    // 计算并拼接CRC
    return "$dataTmp${CRCUtils.getReverseCRC(ByteUtils.hex2byte(dataTmp))}"
}

/**
 * 格式化16进制字符串，每两位增加空格
 */
fun String.format16Str(): String = Pattern.compile("(.{2})").matcher(this).replaceAll("$1 ")

/**
 * 十六进制字符串 转 十进制Int（含正负处理）
 *
 * 十六进制字符串示例："ffffff9c"
 * 转十进制Int：-100
 */
fun String.hex2Int(): Int = BigInteger(this, 16).toInt()

/**
 * 十进制字符串 转 十六进制字符串
 */
@Throws(NumberFormatException::class)
fun String.int2Hex() = Integer.parseInt(this).toFullHex()

/**
 * 去除所有空格
 */
fun String.removeSpaces(): String = this.replace(" ", "")

/**
 * 字符串转换为16进制字符串
 * 例："进料：串口打开失败" ——> "e8bf9be69699efbc9ae4b8b2e58fa3e68993e5bc80e5a4b1e8b4a5"
 */
fun String.encodeHex(): String {
    val bytes = this.toByteArray()
    val sb = StringBuilder(bytes.size * 2)
    //转换hex编码
    bytes.forEach { sb.append(Integer.toHexString(it + 0x800).substring(1)) }
    return sb.toString()
}