package com.sange.easy.storageLite

/**
 * 轻量级存储
 *
 * @author ssq
 */
interface IStorage {

    /**
     * 存放String
     */
    fun putString(key: String, value: String)

    /**
     * 获取String
     */
    fun getString(key: String, defaultValue: String = ""): String

    /**
     * 存放Int
     */
    fun putInt(key: String, value: Int)

    /**
     * 获取Int
     */
    fun getInt(key: String, defaultValue: Int = -1): Int

    /**
     * 存放Boolean
     */
    fun putBoolean(key: String, value: Boolean)

    /**
     * 获取Boolean
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
}