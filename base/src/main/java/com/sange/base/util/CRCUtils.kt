package com.sange.base.util

/**
 * CRC校验码工具类
 *
 * @author ssq
 */
object CRCUtils {

    /**
     * 计算CRC16校验码
     *
     * @param bytes 数据
     */
    private fun getCRC(bytes: ByteArray): String {
        var crc = 0x0000ffff
        val polynomial = 0x0000a001
        var j: Int
        var i = 0
        while (i < bytes.size) {
            crc = crc xor (bytes[i].toInt() and 0x000000ff)
            j = 0
            while (j < 8) {
                if (crc and 0x00000001 != 0) {
                    crc = crc shr 1
                    crc = crc xor polynomial
                } else {
                    crc = crc shr 1
                }
                j++
            }
            i++
        }
        return crc.toFullHex()
    }

    /**
     * 获取反向CRC 高低位转换
     */
    fun getReverseCRC(bytes: ByteArray): String{
        val crc = getCRC(bytes)
        val len = crc.length
        return "${crc.substring(len-2, len)}${crc.substring(len-4, len-2)}"
    }
}