package com.sange.easy.storageLite

//import com.tencent.mmkv.MMKV

/**
 * 腾讯MMKV 轻量级存储实现类
 *
 * @author ssq
 */
/*
class MmkvImpl : IStorage {
    private val mmkv by lazy { MMKV.defaultMMKV() }

    override fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    override fun getString(key: String, defaultValue: String): String =
        mmkv.decodeString(key, defaultValue)

    override fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int = mmkv.decodeInt(key, defaultValue)

    override fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = mmkv.decodeBool(key, defaultValue)
}*/
