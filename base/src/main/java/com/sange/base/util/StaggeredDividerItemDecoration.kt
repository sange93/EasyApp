package com.sange.base.util

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 瀑布流布局间隔
 *
 * @author ssq
 */
class StaggeredDividerItemDecoration(val context: Context, private val interval: Float): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        val interval = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.interval,
        context.resources.displayMetrics)
        // 中间间隔
        if(spanIndex % 2 == 0){
            outRect.left = interval.toInt()
        }else{
            // item为奇数位，设置其左间隔
            outRect.left = interval.toInt()
            outRect.right = interval.toInt()
        }
        // 下方间隔
        outRect.bottom = interval.toInt()
    }
}