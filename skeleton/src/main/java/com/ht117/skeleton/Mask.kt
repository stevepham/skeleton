package com.ht117.skeleton

import android.graphics.*
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class ShimmerMask(private val parent: View,
                  private val config: ShimmerConfig): IMask {

    private var freq = (1_000F / parent.context.refreshRate() * 0.9F).toLong()
    private val width = parent.width.toFloat()
    private val matrix = Matrix()
    private val gradient by lazy {
        val radians = Math.toRadians(config.angle.toDouble()).toFloat()
        LinearGradient(0F, 0F, cos(radians) * width, sin(radians) * width,
            intArrayOf(config.maskColor, config.shimmerColor, config.maskColor), null, Shader.TileMode.CLAMP)
    }
    private val paint = Paint().also {
        it.shader = gradient
        it.isAntiAlias = true
    }
    private val bitmap = Bitmap.createBitmap(parent.width, parent.height, Bitmap.Config.ALPHA_8)
    private val canvas = Canvas(bitmap)
    private var job: Job? = null
    private var scope = CoroutineScope(context = Dispatchers.Main)

    override fun invalidate() {
        if (parent.isAttachedToWindowCompat() && parent.visibility == View.VISIBLE) {
            start()
        } else {
            stop()
        }
    }

    override fun start() {
        if (job == null) {
            job = scope.launch {
                while (true) {
                    updateShimmer()
                    delay(freq)
                }
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, 0F, 0F, paint)
    }

    private fun draw(rectF: RectF, cornerRad: Float, paint: Paint) {
        canvas.drawRoundRect(rectF, cornerRad, cornerRad, paint)
    }

    private fun draw(rect: Rect, paint: Paint) {
        canvas.drawRect(rect, paint)
    }

    fun mask(root: ViewGroup, rad: Float = 0F) {
        val xferPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
            isAntiAlias = rad > 0
        }
        mask(root, root, xferPaint, rad)
    }

    private fun mask(view: View, root: ViewGroup, paint: Paint, rad: Float) {
        (view as? ViewGroup)?.let { viewGroup ->
            viewGroup.allViews().forEach { view ->
                mask(view, root, paint, rad)
            }
        }?: maskView(view, root, paint, rad)
    }

    private fun maskView(view: View, root: ViewGroup, paint: Paint, rad: Float) {
        val rect = Rect()
        view.getDrawingRect(rect)
        root.offsetDescendantRectToMyCoords(view, rect)

        if (rad > 0) {
            val rectF = RectF(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat())
            draw(rectF, rad, paint)
        } else {
            draw(rect, paint)
        }
    }

    private fun updateShimmer() {
        val offset = currentOffset()
        matrix.setTranslate(offset, 0F)
        paint.shader.setLocalMatrix(matrix)
        parent.invalidate()
    }

    private fun currentOffset(): Float {
        val progress = currentProgress()
        val offset = width * 2
        val min = -offset
        val max = width + offset
        return progress * (max - min) + min
    }

    private fun currentProgress(): Float {
        val current = System.currentTimeMillis().toDouble()
        val divisor = floor(current / config.duration)
        val start = config.duration * divisor
        val end = start + config.duration
        val percentage = (current - start) / (end - start)
        return percentage.toFloat()
    }
}