package com.sange.base.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.sange.base.R
import kotlin.math.max

/**
 * 圆角ImageView
 * app:image_view_radius="@dimen/corner_num"
 * @author ssq
 */
class RoundAngleImageView : AppCompatImageView {
    private var widthF = 0f
    private var heightF = 0f
    private var defaultRadius = 0f
    private var radius = 0f
    private var leftTopRadius = 0f
    private var rightTopRadius = 0f
    private var rightBottomRadius = 0f
    private var leftBottomRadius = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        // 不适配 小于18的系统
        /*if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }*/
        val defaultRadiusInt = defaultRadius.toInt()
        // 读取配置
        val array = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView)
        radius = array.getDimensionPixelOffset(R.styleable.RoundAngleImageView_image_view_radius, defaultRadiusInt).toFloat()
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.RoundAngleImageView_left_top_radius, defaultRadiusInt).toFloat()
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.RoundAngleImageView_right_top_radius, defaultRadiusInt).toFloat()
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundAngleImageView_right_bottom_radius, defaultRadiusInt).toFloat()
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundAngleImageView_left_bottom_radius, defaultRadiusInt).toFloat()

        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius
        }
        array.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        widthF = width.toFloat()
        heightF = height.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        val maxLeft = max(leftTopRadius, leftBottomRadius)
        val maxRight = max(rightTopRadius, rightBottomRadius)
        val minWidth = maxLeft + maxRight
        val maxTop = max(leftTopRadius, rightTopRadius)
        val maxBottom = max(leftBottomRadius, rightBottomRadius)
        val minHeight = maxTop + maxBottom
        if (width >= minWidth && height > minHeight) {
            val path = Path()
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0f)
            path.lineTo(width - rightTopRadius, 0f)
            path.quadTo(widthF, 0f, width.toFloat(), rightTopRadius)

            path.lineTo(widthF, height - rightBottomRadius)
            path.quadTo(widthF, heightF, width - rightBottomRadius, heightF)

            path.lineTo(leftBottomRadius, heightF);
            path.quadTo(0f, heightF, 0f, height - leftBottomRadius)

            path.lineTo(0f, leftTopRadius)
            path.quadTo(0f, 0f, leftTopRadius, 0f)

            canvas?.clipPath(path)
        }
        super.onDraw(canvas)
    }
}