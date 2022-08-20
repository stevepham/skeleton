package com.ht117.skeleton

import android.graphics.Canvas
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt

interface ISkeleton {
    fun showSkeleton()
    fun showOriginal()
    fun isSkeleton(): Boolean
}

class SkeletonLayout(originalView: View,
                     private val config: ShimmerConfig): FrameLayout(originalView.context), ISkeleton {

    private var mask: IMask? = null
    private var isSkeleton = false
    private var isRendered = false

    init {
        addView(originalView)
    }

    override fun showSkeleton() {
        isSkeleton = true
        if (isRendered && childCount > 0) {
            allViews().forEach { it.invisible() }
            setWillNotDraw(false)
            invalidateMask()
            mask?.invalidate()
        }
    }

    override fun showOriginal() {
        isSkeleton = false
        if (childCount > 0) {
            allViews().forEach { it.visible() }
            mask?.stop()
            mask = null
        }
    }

    override fun isSkeleton() = isSkeleton

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        isRendered = true
        if (isSkeleton) {
            showSkeleton()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        mask?.invalidate()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mask?.start()
        } else {
            mask?.stop()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isRendered) {
            invalidateMask()
            mask?.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mask?.stop()
    }

    override fun onDraw(canvas: Canvas) {
        mask?.draw(canvas)
    }

    private fun invalidateMask() {
        if (isRendered) {
            mask?.stop()
            mask = null
            if (isSkeleton) {
                if (width > 0 && height > 0) {
                    mask = ShimmerMask(this, config).also {
                        it.mask(this)
                    }
                }
            }
        }
    }
}