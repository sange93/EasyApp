# base库
基础库，所有APP通用。
架构：Jetpack MVVM
## 使用规范
Application --必须继承--> BaseApplication
Activity --必须继承--> BaseViewModelActivity；
Dialog --必须继承--> BaseDialogFragment
LoadState：页面加载状态，用于监听耗时操作的状态，动态改变页面UI；
## 常用工具类（util包）：
exception包：异常处理工具类，使用说明详见包内README.md;
AesUtils：AES加解密工具类；使用前请调用AesUtils.init()初始化，建议在Application中调用；
EncodeUtils：编解码工具类；
LogUtils：日志工具类；
ProcessUtils：进程工具类；
ToastUtils：Toast提示工具类
IntExt：Int整型扩展类；
ViewModelExt：ViewModel扩展类
TopLevelUtils：顶级方法工具类；

## 注意
1、gradle sync 报错:
```
Failed to resolve: com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.6
Show in Project Structure dialog
Affected Modules: app, base, easy
```
请在项目build.gradle添加仓库
```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
2、AS编译报错:
```gradle
More than one file was found with OS independent path META-INF/library_release.kotlin_module
```
请在app模块添加配置
```gradle
android {
    packagingOptions {
        exclude 'META-INF/library_release.kotlin_module'
    }
}
```