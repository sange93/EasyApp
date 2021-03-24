# easy库
抽象易组件：抽象组件调用具体组件，统一调用规范，并减少应用对组件的直接依赖。
## 使用
1、首先在app模块的Application中调用Easy.init()方法初始化组件。  
Easy类：组件的总入口。原则上，所有功能均需通过Easy获取实例。  
2、如果自定义异常处理器，请继承此EasyExceptionHandler类。
3、混淆配置  
如果你的项目开启了混淆，则需要在你的app模块的proguard-rules.pro中[加入此配置](proguard-rules.pro)
## 模块说明
back包：页面侧滑返回，使用说明详见包内README.md;  
imageLoader包：图片加载框架，使用请调用Easy.getImageLoaderUtil()获取实例；  
json包：json数据解析框架，使用请调用Easy.getJsonUtil()获取实例；  
net包：网络通讯框架，包含HTTPS接口通讯和WebSocket长连接通讯，使用说明详见包内README.md;  
storageLite包：轻量级存储框架，使用请调用Easy.getStorageUtil()获取实例；