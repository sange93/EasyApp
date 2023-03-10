package com.sange.easy.storageLite

import android.content.Context
import com.sange.easy.Easy

/**
 * SharedPreferences 实现类
 *
 * @author ssq
 * @param fileName 数据文件名，默认为"sp_data"
 */
class SpImpl(fileName: String): IStorage {
    private val mSp by lazy { Easy.getAppContext().getSharedPreferences(fileName, Context.MODE_PRIVATE) }

    override fun putString(key: String, value: String) {
        mSp.edit().putString(key,value).apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return mSp.getString(key,defaultValue) ?: defaultValue
    }

    override fun putInt(key: String, value: Int) {
        mSp.edit().putInt(key,value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mSp.getInt(key,defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mSp.edit().putBoolean(key,value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSp.getBoolean(key,defaultValue)
    }
}