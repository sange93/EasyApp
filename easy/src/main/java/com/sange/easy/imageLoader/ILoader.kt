package com.sange.easy.imageLoader

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import java.io.File

/**
 *  图片加载接口（第三方框架需要实现此接口）
 * Created by ssq on 2018/3/7.
 */
interface ILoader {

    /**
     * 加载普通图片,无占位图
     */
    fun displayImage(url: Any?, imageView: ImageView)

    /**
     * 加载普通图片,自定义占位图
     */
    fun displayImage(url: Any?, imageView: ImageView, @DrawableRes resourceId: Int)

    /**
     * 加载缩略图
     */
    fun displayThumbnailImage(url: Any?, imageView: ImageView)

    /**
     * 获取图片 bitmap (线程同步)
     */
    fun getBitmapSync(url: Any): Bitmap

    /**
     * 获取图片 bitmap (线程同步)
     */
    fun getBitmapSync(url: Any, width: Int, height: Int): Bitmap

    /**
     * 获取图片 bitmap (异步)
     */
    fun getBitmap(url: Any, callBack: ILoaderCallBack<Bitmap>)

    /**
     * 获取图片 bitmap (异步)
     */
    fun getBitmap(url: Any, callBack: ILoaderCallBack<Bitmap>, width: Int, height: Int)

    /**
     * 获取图片 file (异步)
     */
    fun getFile(url: Any, callBack: ILoaderCallBack<File>)

    /**
     * 继续所有的加载请求
     */
    fun resumeRequestsRecursive()

    /**
     * 清除view的缓存
     */
    fun clear(view: View)

    /**
     * 清除内存缓存
     */
    fun clearMemory()
}