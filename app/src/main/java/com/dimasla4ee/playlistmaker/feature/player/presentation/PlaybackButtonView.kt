package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.Keep
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withScale
import com.dimasla4ee.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var state: State = State.PAUSED
        set(value) {
            if (field == value) return

            val oldState = field
            field = value

            handleStateChange(value)
            handleIconTransition(oldState, value)
            invalidate()
        }
    private var circleScale = DEFAULT_CIRCLE_SCALE
    private var valueAnimator = ValueAnimator().apply {
        addUpdateListener {
            circleScale = it.animatedValue as Float
            invalidate()
        }
    }
    private var center = PointF()
    private var radius = 0f
    private var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.playbackFabBackground)
    }
    var drawableScale: Float = DEFAULT_DRAWABLE_SCALE
    var drawablePlay: Drawable? = null
        set(value) {
            field = value
            drawablePlay?.setTint(iconTint)
        }
    var drawablePause: Drawable? = null
        set(value) {
            field = value
            drawablePause?.setTint(iconTint)
        }
    var iconTint: Int = context.getColor(R.color.playbackFabIcon)
        set(value) {
            field = value
            drawablePause?.setTint(value)
            drawablePlay?.setTint(value)
        }
    private var iconBounds = Rect()

    @Keep
    private var iconScale = DEFAULT_ICON_SCALE
        set(value) {
            field = value
            invalidate()
        }
    private val iconScaleAnimator = ObjectAnimator.ofFloat(
        this,
        "iconScale",
        MIN_ICON_SCALE,
        DEFAULT_ICON_SCALE
    ).apply {
        duration = 200
        interpolator = OvershootInterpolator()
    }

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ) {
            drawablePlay = getDrawable(R.styleable.PlaybackButtonView_drawablePlay)?.mutate()
            drawablePause = getDrawable(R.styleable.PlaybackButtonView_drawablePause)?.mutate()
            drawableScale = getFloat(
                R.styleable.PlaybackButtonView_drawableScale,
                DEFAULT_DRAWABLE_SCALE
            )
        }

        isClickable = true
        isFocusable = true
    }

    private fun handleStateChange(newState: State) = with(valueAnimator) {
        cancel()
        when (newState) {
            State.LOADING -> {
                setFloatValues(
                    DEFAULT_CIRCLE_SCALE,
                    MIN_SURFACE_SCALE,
                    DEFAULT_CIRCLE_SCALE
                )
                repeatCount = ValueAnimator.INFINITE
                duration = PULSE_DURATION
            }

            else -> {
                setFloatValues(
                    circleScale,
                    DEFAULT_CIRCLE_SCALE
                )
                repeatCount = 0
                duration = RETURN_DURATION
            }
        }
        start()
    }

    private fun handleIconTransition(old: State, new: State) {
        if (old == State.LOADING && new != State.LOADING) {
            iconScale = MIN_ICON_SCALE
            iconScaleAnimator.start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.set(w / 2f, h / 2f)
        radius = w / 2f

        val drawableSize = (w * drawableScale).toInt()
        val margin = (w - drawableSize) / 2
        iconBounds.set(margin, margin, margin + drawableSize, margin + drawableSize)

        drawablePlay?.bounds = iconBounds
        drawablePause?.bounds = iconBounds
    }

    override fun onDraw(canvas: Canvas) {
        val currentRadius = radius * circleScale
        canvas.drawCircle(center.x, center.y, currentRadius, backgroundPaint)

        val icon = when (state) {
            State.LOADING -> null
            State.PLAYING -> drawablePause
            State.PAUSED -> drawablePlay
        }

        canvas.withScale(iconScale, iconScale, center.x, center.y) {
            icon?.draw(this)
        }
    }

    override fun onAttachedToWindow() {
        if (state == State.LOADING) valueAnimator.start()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        valueAnimator.cancel()
        iconScaleAnimator.cancel()
        super.onDetachedFromWindow()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean = when (event.action) {
        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_CANCEL -> {
            true
        }

        MotionEvent.ACTION_UP -> {
            performClick()
            true
        }

        else -> {
            super.onTouchEvent(event)
        }
    }

    enum class State {
        LOADING,
        PLAYING,
        PAUSED
    }

    private companion object {
        const val DEFAULT_DRAWABLE_SCALE = 0.5f
        const val DEFAULT_CIRCLE_SCALE = 1f
        const val MIN_SURFACE_SCALE = 0.8f

        const val RETURN_DURATION = 300L
        const val PULSE_DURATION = 1500L

        const val DEFAULT_ICON_SCALE = 1f
        const val MIN_ICON_SCALE = 0.5f
    }

}
