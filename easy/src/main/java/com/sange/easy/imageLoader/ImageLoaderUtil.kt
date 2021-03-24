package com.sange.easy.imageLoader

/**
 * 图片加载 工具类
 * Created by ssq on 2018/3/7.
 */
object ImageLoaderUtil {

    fun with(any: Any): ILoader {
        // 更换第三方框架时需要修改实现类
        return ImageLoaderProxy(GlideLoaderImpl(any))
    }
}