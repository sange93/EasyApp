package com.sange.base.util

import android.os.Environment
import com.sange.base.BaseApplication
import java.io.File
import java.math.BigDecimal

/**
 * 缓存工具类
 *
 * @author ssq
 */
object CacheUtil {
    // 存储单位：KB
    private const val UNIT_KB = "KB"
    private const val UNIT_MB = "MB"
    private const val UNIT_GB = "GB"
    private const val UNIT_TB = "TB"

    /**
     * 获取缓存大小
     */
    fun getTotalCacheSize(): String {
        var cacheSize = getFolderSize(BaseApplication.instance.cacheDir)
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            BaseApplication.instance.externalCacheDir?.let {
                cacheSize += getFolderSize(it)
            }
        }
        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 清除所有缓存
     */
    fun clearAllCache() {
        deleteDir(BaseApplication.instance.cacheDir)
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            BaseApplication.instance.externalCacheDir?.let { deleteDir(it) }
        }
    }

    /**
     * 删除文件夹
     */
    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children: Array<out String>? = dir.list()
            children?.forEach {
                val success = deleteDir(File(dir, it))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

    /**
     * 获取文件
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList: Array<out File> = file.listFiles() ?: arrayOf()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size += if (fileList[i].isDirectory) {
                    getFolderSize(fileList[i])
                } else {
                    fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "0$UNIT_KB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + UNIT_KB
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + UNIT_MB
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + UNIT_GB
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
            .toString() + UNIT_TB
    }

    /**
     * 是否超出多少MB
     *
     * @param size 缓存大小
     * @param maxSize 最大缓存大小 单位：MB
     */
    fun isMoreThanOfMB(size: String, maxSize: Int): Boolean {
        // 最大不超过500MB
        if (size.endsWith(UNIT_MB) && size.substringBefore(UNIT_MB).toFloat() > maxSize) {
            return true
        } else if (size.endsWith(UNIT_GB) || size.endsWith(UNIT_TB)) {
            return true
        }
        return false
    }
}