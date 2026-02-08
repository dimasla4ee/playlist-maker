package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.dimasla4ee.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    //    private val minViewSize = 24 TODO: dp size
    private var isPlaying = false
    private var center = 0f at 0f
    private var radius = 0f
    private var backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.playbackFabBackground)
    }
    private var drawablePlay: Drawable? = null
    private var drawablePause: Drawable? = null
    private var drawableScale: Float? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                drawablePlay = getDrawable(R.styleable.PlaybackButtonView_drawablePlay)
                drawablePause = getDrawable(R.styleable.PlaybackButtonView_drawablePause)
                drawableScale = getFloat(
                    R.styleable.PlaybackButtonView_drawableScale,
                    DEFAULT_SCALE
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center = (measuredWidth / 2f) at (measuredHeight / 2f)
        radius = measuredWidth / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, radius, backgroundPaint)

        val drawable = (if (isPlaying) drawablePause else drawablePlay) ?: return

        val color = context.getColor(R.color.playbackFabIcon)
        val size = (min(width, height) * drawableScale!!).toInt()
        val left = (width - size) / 2
        val top = (height - size) / 2
        val right = left + size
        val bottom = top + size

        with(drawable) {
            setTint(color)
            setBounds(left, top, right, bottom)
            draw(canvas)
        }
    }

    fun setPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean = when (event.action) {
        MotionEvent.ACTION_DOWN -> true
        MotionEvent.ACTION_UP -> {
            performClick()
            true
        }

        MotionEvent.ACTION_CANCEL -> true

        else -> super.onTouchEvent(event)
    }

    private data class Point(
        val x: Float = 0f,
        val y: Float = 0f
    )

    private infix fun Float.at(y: Float): Point = Point(this, y)

    private companion object {
        const val DEFAULT_SCALE = 0.5f
    }

}
