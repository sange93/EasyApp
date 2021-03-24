package com.sange.easy.net.bean

/**
 * Json对象基类
 */
open class BaseBean(httpData: HttpData) {
    var code = -1
    var msg = ""

    init {
        code = httpData.code
        msg = httpData.message
    }
}