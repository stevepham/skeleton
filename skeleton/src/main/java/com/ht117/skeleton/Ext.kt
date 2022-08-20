package com.ht117.skeleton

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.ht117.skeleton.rv.SkeletonRecyclerView

internal fun Context.refreshRate(): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display?.refreshRate?: 60F
    } else {
        val wnd = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        wnd?.defaultDisplay?.refreshRate?: 60F
    }
}

internal fun ViewGroup.allViews(): List<View> {
    return (0 until childCount).map { getChildAt(it) }
}

internal fun View.isAttachedToWindowCompat() = ViewCompat.isAttachedToWindow(this)

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.generateSkeleton(config: ShimmerConfig = ShimmerConfig(),
                          @LayoutRes layoutId: Int = -1,
                          itemCount: Int = 3): ISkeleton {
    return when (this) {
        is RecyclerView -> {
            require(layoutId != -1) { "This view required layout id" }
            SkeletonRecyclerView(this, layoutId, itemCount)
        }
        else -> {
            val parent = (parent as? ViewGroup)
            val index = parent?.indexOfChild(this)?: 0
            val params = layoutParams

            parent?.removeView(this)
            val skeleton = SkeletonLayout(this, config)

            if (params != null) {
                skeleton.layoutParams = params
            }
            parent?.addView(skeleton, index)
            skeleton
        }
    }
}