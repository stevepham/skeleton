package com.ht117.skeleton

import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt

internal interface IMask {
    fun invalidate() = Unit

    fun start() = Unit

    fun stop() = Unit

    fun draw(canvas: Canvas)
}

data class ShimmerConfig(@ColorInt val maskColor: Int = Color.parseColor("#f5f5f5"),
                         @ColorInt val shimmerColor: Int = Color.parseColor("#b0b0b0"),
                         val duration: Long = 1_000L,
                         val angle: Int = 30)