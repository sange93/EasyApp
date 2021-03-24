package com.sange.easy.net.demo

import com.sange.easy.net.bean.BaseBean
import com.sange.easy.net.bean.HttpData

/**
 * 用户信息（示例Bean）
 *
 * @author ssq
 */
class UserData(httpData: HttpData): BaseBean(httpData) {
    var data = User()

    init {
        data = httpData.getObjectData(User::class.java)
    }
}