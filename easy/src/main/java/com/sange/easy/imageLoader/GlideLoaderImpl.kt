package com.sange.easy.imageLoader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sange.base.util.LogUtils
import com.sange.easy.Easy
import com.sange.easy.R
import java.io.File

/**
 * glide 框架加载 实现类
 * Created by ssq on 2018/3/7.
 */
open class GlideLoaderImpl(any: Any) : ILoader {
    protected var mRequestManager: RequestManager? = null

    init {
        when (any) {
            is Activity -> mRequestManager = Glide.with(any)
            is FragmentActivity -> mRequestManager = Glide.with(any)
            is View -> mRequestManager = Glide.with(any)
            is Fragment -> mRequestManager = Glide.with(any)
            is Context -> mRequestManager = Glide.with(any)
            else -> LogUtils.e(getAppContext().getString(R.string.easy_error_parameter_fail))
        }
    }

    // 缩略图配置
    private var mThumbnailOptions = RequestOptions()
            .centerCrop()
            .priority(Priority.LOW)
            .skipMemoryCache(true)// 不使用缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)// 不使用缓存

    // 获取位图配置
    private var mBitmapOptions = RequestOptions()
            .centerCrop()
            .priority(Priority.HIGH)
            .skipMemoryCache(true)// 不使用缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)// 不使用缓存

    private fun getAppContext() = Easy.getAppContext()

    override fun displayImage(url: Any?, imageView: ImageView) {
        val options = RequestOptions()
            .centerInside()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        mRequestManager!!.load(url).apply(options).into(imageView)
    }

    override fun displayImage(url: Any?, imageView: ImageView, resourceId: Int) {
        val options = RequestOptions()
            .fitCenter()
            .placeholder(resourceId)
            .error(resourceId)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        mRequestManager!!.load(url).apply(options).into(imageView)
    }

    override fun displayThumbnailImage(url: Any?, imageView: ImageView) {
        mRequestManager!!.load(url).apply(mThumbnailOptions).into(imageView)
    }

    override fun getBitmapSync(url: Any): Bitmap {
        return mRequestManager!!.asBitmap().load(url).submit().get()
    }

    override fun getBitmapSync(url: Any, width: Int, height: Int): Bitmap {
        return mRequestManager!!.asBitmap().load(url).submit(width, height).get()
    }

    override fun getBitmap(url: Any, callBack: ILoaderCallBack<Bitmap>) {
        mRequestManager!!.asBitmap().load(url).into(MyBitmapSimpleTarget(callBack))
    }

    override fun getBitmap(url: Any, callBack: ILoaderCallBack<Bitmap>, width: Int, height: Int) {
        mRequestManager!!.asBitmap().load(url).apply(mBitmapOptions).into(MyBitmapSimpleTarget(callBack, width, height))
    }

    override fun getFile(url: Any, callBack: ILoaderCallBack<File>) {
        mRequestManager!!.asFile().load(url).into(MyFileSimpleTarget(callBack))
    }

    override fun preLoad(url: Any) {
        mRequestManager!!.load(url).diskCacheStrategy(DiskCacheStrategy.DATA).preload()
    }

    override fun resumeRequestsRecursive() {
        mRequestManager!!.resumeRequestsRecursive()
    }

    override fun clear(view: View) {
        mRequestManager!!.clear(view)
    }

    override fun clearMemory() {
        Glide.get(getAppContext()).clearMemory()
    }

    /**
     * Bitmap 回调
     */
    inner class MyBitmapSimpleTarget : CustomTarget<Bitmap> {
        private var mCallBack: ILoaderCallBack<Bitmap>

        constructor(callBack: ILoaderCallBack<Bitmap>) : super() {
            mCallBack = callBack
        }

        constructor(callBack: ILoaderCallBack<Bitmap>, width: Int, height: Int) : super(width, height) {
            mCallBack = callBack
        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            mCallBack.onSuccess(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            mCallBack.onFail(getAppContext().getString(R.string.easy_error_load_image_fail))
        }
    }

    /**
     * File 回调
     */
    inner class MyFileSimpleTarget(callBack: ILoaderCallBack<File>) : CustomTarget<File>() {
        private val mCallBack = callBack

        override fun onResourceReady(resource: File, transition: Transition<in File>?) {
            mCallBack.onSuccess(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            mCallBack.onFail(getAppContext().getString(R.string.easy_error_load_image_fail))
        }
    }
}