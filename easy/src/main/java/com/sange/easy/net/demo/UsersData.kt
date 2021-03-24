package com.sange.easy.net.demo

import com.sange.easy.net.bean.BaseBean
import com.sange.easy.net.bean.HttpData

/**
 * 用户集合（示例Bean）
 *
 * @author ssq
 */
class UsersData(httpData: HttpData) : BaseBean(httpData) {
    var data = arrayListOf<User>()

    init {
        data.addAll(httpData.getListData(User::class.java))
    }
}