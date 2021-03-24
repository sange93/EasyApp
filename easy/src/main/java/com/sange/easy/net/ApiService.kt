package com.sange.easy.net

import com.sange.easy.net.bean.HttpData
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 网络服务接口(协程)
 *
 * @author ssq
 * @JvmSuppressWildcards 用来注解类和方法，使得被标记元素的泛型参数不会被编译成通配符?
 */
@JvmSuppressWildcards
interface ApiService {

    /*
     * 通用异步请求 只需要解析BaseBean
     */
//    @FormUrlEncoded
//    @POST("wxarticle/chapters")
//    suspend fun request(@FieldMap map: Map<String, Any>): BaseBean

    /*
     * 上传图片
     * @param url 可选，不传则使用默认值
     * @param imgPath 图片路径
     * @param map     参数
     */
//    @Multipart
//    @POST
//    fun uploadImgAsync(@Url url: String = "${ApiConstant.UPLOAD_IMAGE_URL}FileUpload.php", @PartMap imgPath: Map<String, RequestBody>, @QueryMap map: Map<String, Any>): Deferred<Response<UploadImgEntity>>

    /**
     * 下载文件
     * @param fileUrl 文件地址 (这里的url可以是全名，也可以是基于baseUrl拼接的后缀url)
     * @return
     */
    @Streaming
    @GET
    suspend fun downloadFileAsync(@Url fileUrl: String): ResponseBody//Deferred<ResponseBody>

    /**
     * 通用异步请求
     */
    @FormUrlEncoded
    @POST("v1")
    suspend fun request(@Field("encrypt") encrypt: String, @Field("iv") iv: String): HttpData
}