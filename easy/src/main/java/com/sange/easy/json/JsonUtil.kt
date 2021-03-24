package com.sange.easy.json

/**
 * Json解析处理 工具类
 *
 * Created by ssq on 2021/2/5.
 */
object JsonUtil {

    fun impl(): IJson {
        // 更换第三方框架时需要修改实现类
        return GsonImpl()
    }
}