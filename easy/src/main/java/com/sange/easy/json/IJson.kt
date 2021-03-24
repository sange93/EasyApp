package com.sange.easy.json

interface IJson {

    /**
     * 对象转Json
     *
     * @param src 将要被转的对象
     */
    fun toJson(src: Any): String

    /**
     * 对象转Json
     *
     * @param src 将要被转的对象
     * @param skipField 排除的参数
     */
    fun toJson(src: Any, vararg skipField: String): String

    /**
     * 解析Json
     *
     * @param json 要解析的json
     * @param clazz 转换的数据类型
     */
    @Throws(JsonException::class)
    fun <T> fromJson(json: String, clazz: Class<T>): T

    /**
     * 解析Json串为集合
     *
     * @param json Json字符串
     * @param clazz 类型
     * @param <T> 类型
     * @return 集合
     */
    fun <T> fromJson2List(json: String, clazz: Class<T>): List<T>
}