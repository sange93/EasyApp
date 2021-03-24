# exception 异常工具使用说明

## 1、使用
在异常方法内调用
```kotlin
ExceptionUtil.getExceptionHandler().catchException(throwable)
```
ps: 默认使用的异常处理器是BaseExceptionHandler
## 2、自定义异常处理器
（1）继承BaseExceptionHandler，重写方法onException(e: Throwable)方法，示例代码如下：
```kotlin
class ExceptionHandler: BaseExceptionHandler() {

    override fun onException(e: Throwable): Boolean {
        when(e){
            is NullPointerException -> {
                // 这里处理异常...,然后返回true
                return true
            }
            is IllegalStateException -> {
                // 这里处理异常...,然后返回true
                return true
            }
        }
        // 未处理异常 返回false
        return false
    }
}
```
（2）