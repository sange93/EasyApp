# net网络通讯控件使用说明

## 一、HTTPS 接口通讯

### 1、使用默认的ApiService
（1）数据Bean的定义请参考net.demo包下的示例代码,
（2）网络资源库请继承Repository
Repository子类中定义接口示例代码：
```kotlin 
    object MyRepository: Repository(){
        
        /**
         * 提供网络请求根地址
         * ps:网址必须以"/"结尾
         * e.g. "https://www.你的地址.com/"
         */
        override fun providerBaseUrl(): String = "https://www.你的地址.com/"
    
        /**
         * 获取用户信息
         *
         * @param param （未加密的）请求参数
         */
        suspend fun getUser(param: JSONObject) = request(param, UserData::class.java)
    }
```
ViewModel类中接口调用示例代码：
```kotlin
/**
 * 主页ViewModel
 */
class MainActivityVM : BaseViewModel() {
    // 设备信息
    val mUser = MutableLiveData<UserData>()

    /**
     * 获取用户信息
     */
    fun getUser() = launch({
        loadState.value = LoadState.Loading()
        val param = JSONObject().apply {
            // 请求的接口名
            put("request_fun", "get_user")
            // 请求参数...
            put("id", userId)
        }
        mUser.value = Repository.getUser(param)
        loadState.value = LoadState.Success()
    }, {
        loadState.value = LoadState.Fail()
    })
}
```
### 2、自定义ApiService
（1）根据具体业务场景定义接口interface类；
（2）自定义Repository类继承BaseRepository，并实现抽象方法providerBaseUrl()与providerInterface()，可参考默认的Repository类；

##  二、WebSocket通讯
