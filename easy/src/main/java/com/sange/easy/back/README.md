# back控件使用说明

在Activity类加入以下代码：
```kotlin 
    /** 侧滑返回 */
    private lateinit var mSwipeBackHelper: SwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeBackHelper = SwipeBackHelper(this)
    }

    /**
     * Touch手势分发
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
                // 使用侧滑处理
                return mSwipeBackHelper.dispatchTouchEvent(it) {
                    // 没有触发侧滑事件，继续执行其他逻辑
                    super.dispatchTouchEvent(ev)
                }
        }
        return super.dispatchTouchEvent(ev)
    }
```
