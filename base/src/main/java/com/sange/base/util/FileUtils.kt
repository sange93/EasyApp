package com.sange.base.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import com.sange.base.BaseApplication
import java.io.*
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文件工具类
 *
 * @author ssq
 */
object FileUtils {

    private val hexDigits =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * 获取 app 缓存路径
     * Android4.4+ 不需要读写权限
     */
    fun getCachePath(): String = getCachePathFile().path

    /**
     * 获取 app 缓存路径
     * Android4.4+ 不需要读写权限
     */
    fun getCachePathFile(): File =
        // 判断外部存储状态可用，或者外部存储不可移除
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            // 外部存储可用
            BaseApplication.instance.externalCacheDir ?: BaseApplication.instance.cacheDir
        } else {
            BaseApplication.instance.cacheDir
        }

    /**
     * 获取下载路径
     * 需要动态申请存储权限
     *
     * @param appFileName APP文件夹名
     */
    @Suppress("DEPRECATION")
    fun getDownloadPath(appFileName: String): String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separatorChar + appFileName


    /**
     * 拷贝文件
     * @param fromUri 源文件地址
     * @param toPath 保存的文件路径
     * @return 保存后的文件
     */
    fun copyFile(fromUri: Uri, toPath: String): File? {
        val file = File(toPath)
        var fos: FileOutputStream? = null
        var fis: FileInputStream? = null
        try {
            BaseApplication.instance.contentResolver.openFileDescriptor(fromUri, "r")?.let {
                val bytes = ByteArray(1024)
                fis = FileInputStream(it.fileDescriptor)
                fos = FileOutputStream(file)
                if (fis != null && fos != null) {
                    var length = fis?.read(bytes) ?: 0
                    while (length != -1) {
                        fos?.write(bytes, 0, length)
                        length = fis?.read(bytes) ?: -1
                    }
                    fos?.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
                fis?.close()
                return file
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 生成默认文件名
     * 保存图片时 文件名时间格式 不要含有：冒号  会导致微信不能识别图片
     */
    private fun buildFileName(): String = TimeUtils.millis2String(
        System.currentTimeMillis(),
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
    )

    /**
     * 获取文件的MD5值
     */
    fun getFileMD5(file: File): String? {
        if (!file.exists() || !file.isFile || file.length() <= 0) {
            return null
        }
        var inputStream: InputStream? = null
        try {
            val md = MessageDigest.getInstance("MD5")
            inputStream = FileInputStream(file)
            val dataBytes = ByteArray(4096)
            var iRd: Int
            iRd = inputStream.read(dataBytes)
            while (iRd != -1) {
                md.update(dataBytes, 0, iRd)
                iRd = inputStream.read(dataBytes)
            }
            inputStream.close()
            val digest = md.digest()
            return bufferToHex(digest)
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    private fun bufferToHex(bytes: ByteArray): String {
        return bufferToHex(bytes, 0, bytes.size)
    }

    private fun bufferToHex(bytes: ByteArray, m: Int, n: Int): String {
        val sb = StringBuffer(2 * n)
        val k = m + n
        for (l in m until k) {
            appendHexPair(bytes[l], sb)
        }
        return sb.toString()
    }

    private fun appendHexPair(bt: Byte, sb: StringBuffer) {
        val c0 = hexDigits[bt.toInt() and 0xf0 ushr 4]
        val c1 = hexDigits[bt.toInt() and 0x0f]
        sb.append(c0)
        sb.append(c1)
    }

    /**
     * 拷贝Assets资源到磁盘
     */
    fun copyAssetsToStorage(
        context: Context,
        dir: String,
        files: Array<String>,
        loadSuccess: () -> Unit
    ) {
        Thread {
            var outputStream: OutputStream
            var inputStream: InputStream
            val buf = ByteArray(4096)
            files.forEach {
                try {
                    if (File("$dir/$it").exists()) {
                        return@forEach
                    }
                    inputStream = context.assets.open(it)
                    outputStream = FileOutputStream("$dir/$it")
                    var length = inputStream.read(buf)
                    while (length > 0) {
                        outputStream.write(buf, 0, length)
                        length = inputStream.read(buf)
                    }
                    outputStream.close()
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return@Thread
                }
            }
            loadSuccess.invoke()
        }.start()
    }

    /**
     * 读取文件数据
     *
     * @param assetsFileName assets资源文件名
     * @return 数据字符串
     */
    fun readData(assetsFileName: String): String {
        var result = ""
        var inputStream: InputStream? = null
        val outputStream = ByteArrayOutputStream()
        try {
            inputStream = BaseApplication.instance.assets.open(assetsFileName)
            result = readData(inputStream, outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * 从本地文件读取数据
     */
    fun readData(uri: Uri): String {
        var result = ""
        var fis: FileInputStream? = null
        val outputStream = ByteArrayOutputStream()
        try {
            BaseApplication.instance.contentResolver.openFileDescriptor(uri, "r")?.let {
                fis = FileInputStream(it.fileDescriptor)
                fis?.let { inputStream -> result = readData(inputStream, outputStream) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fis?.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * 读取数据
     */
    private fun readData(inputStream: InputStream, outputStream: ByteArrayOutputStream): String {
        var len: Int
        val buffer = ByteArray(1024)
        while (inputStream.read(buffer).also { len = it } != -1) {
            outputStream.write(buffer, 0, len)
        }
        return outputStream.toString()
    }

    /**
     * 跳至选择系统管理器选择文件
     */
    fun chooseFile(activity: FragmentActivity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * 写入文件至Download文件夹下
     *
     * @param data 数据字符串
     * @param appFileName APP文件夹名
     * @param relativePath Download文件下的相对路径，必须以"/"起始，e.g."/test_file.txt"
     * @return 写入成功返回文件路径，否则返回""
     */
    fun writeFile2Download(data: String, appFileName: String, relativePath: String): String {
        val downloadPath = getDownloadPath(appFileName)
        val parentFile = File(downloadPath)
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        val path = downloadPath + relativePath
        return try {
            File(path).writeText(data)
            path
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}