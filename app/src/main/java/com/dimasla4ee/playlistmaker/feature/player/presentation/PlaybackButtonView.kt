package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withScale
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.utils.getAvd
import kotlin.math.min
import kotlin.math.sqrt

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

    private var center = PointF()
    private var radius = 0f
    private var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.playbackFabBackground)
    }
    private var circleScale = DEFAULT_CIRCLE_SCALE
    private var circlePulseAnimator = ValueAnimator().apply {
        addUpdateListener {
            circleScale = it.animatedValue as Float
            invalidate()
        }
    }

    private var iconBounds = Rect()
    var iconTint: Int = context.getColor(R.color.playbackFabIcon)
        set(value) {
            field = value
            avdPausedToPlaying?.setTint(value)
            avdPlayingToPaused?.setTint(value)
        }
    private var iconScale = DEFAULT_ICON_SCALE
    private val iconAppearAnimator = ValueAnimator.ofFloat(
        MIN_ICON_SCALE,
        DEFAULT_ICON_SCALE
    ).apply {
        duration = ANIM_ICON_POPUP_DURATION
        interpolator = OvershootInterpolator()
        addUpdateListener {
            iconScale = it.animatedValue as Float
            invalidate()
        }
    }
    private var currentDrawable: Drawable? = null
    var drawableScale: Float = DEFAULT_DRAWABLE_SCALE
    var drawablePlaying: Drawable? = null
        set(value) {
            field = value
            drawablePlaying?.setTint(iconTint)
        }
    var drawablePaused: Drawable? = null
        set(value) {
            field = value
            drawablePaused?.setTint(iconTint)
        }
    private var avdPausedToPlaying: AnimatedVectorDrawable? = null
        set(value) {
            field = value
            field?.setTint(iconTint)
        }
    private var avdPlayingToPaused: AnimatedVectorDrawable? = null
        set(value) {
            field = value
            field?.setTint(iconTint)
        }

    init {
        with(context) {
            withStyledAttributes(
                attrs,
                intArrayOf(androidx.appcompat.R.attr.iconTint),
                defStyleAttr,
                defStyleRes
            ) {
                val defaultColor = getColor(R.color.playbackFabIcon)
                iconTint = getColor(0, defaultColor)
            }

            withStyledAttributes(
                attrs,
                R.styleable.PlaybackButtonView,
                defStyleAttr,
                defStyleRes
            ) {
                drawableScale = getFloat(
                    R.styleable.PlaybackButtonView_drawableScale,
                    DEFAULT_DRAWABLE_SCALE
                )
                drawablePlaying = getDrawable(
                    R.styleable.PlaybackButtonView_drawablePlaying
                )?.mutate()
                drawablePaused = getDrawable(
                    R.styleable.PlaybackButtonView_drawablePaused
                )?.mutate()
                avdPausedToPlaying = getAvd(R.styleable.PlaybackButtonView_avdPausedToPlaying)
                avdPlayingToPaused = getAvd(R.styleable.PlaybackButtonView_avdPlayingToPaused)
            }
        }

        isClickable = true
        isFocusable = true
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

        val drawableSize = (radius * sqrt(2f) * drawableScale).toInt()
        val margin = (w - drawableSize) / 2
        iconBounds.set(margin, margin, margin + drawableSize, margin + drawableSize)

        updateAllDrawableBounds()
    }

    override fun onDraw(canvas: Canvas) {
        val currentRadius = radius * circleScale
        canvas.drawCircle(center.x, center.y, currentRadius, backgroundPaint)

        if (state == State.LOADING) return

        canvas.withScale(iconScale, iconScale, center.x, center.y) {
            currentDrawable!!.draw(this)
        }
    }

    override fun onAttachedToWindow() {
        if (state == State.LOADING) circlePulseAnimator.start()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        cancelAllAnimations()
        super.onDetachedFromWindow()
    }

    private fun startPulseAnimation(): Unit = circlePulseAnimator.run {
        cancel()
        setFloatValues(
            DEFAULT_CIRCLE_SCALE,
            MIN_CIRCLE_SCALE,
            DEFAULT_CIRCLE_SCALE
        )
        repeatCount = ValueAnimator.INFINITE
        duration = ANIM_PULSE_DURATION
        start()
    }

    private fun startReturnAnimation(): Unit = circlePulseAnimator.run {
        cancel()
        setFloatValues(
            circleScale,
            DEFAULT_CIRCLE_SCALE
        )
        repeatCount = 0
        duration = ANIM_RETURN_DURATION
        start()
    }

    private fun handleStateChange(newState: State) = when (newState) {
        State.LOADING -> startPulseAnimation()
        else -> startReturnAnimation()
    }

    private fun handleIconTransition(
        oldState: State,
        newState: State
    ) {
        currentDrawable = when {
            ((oldState == State.PAUSED) && (newState == State.PLAYING)) -> {
                avdPausedToPlaying?.apply { start() } ?: drawablePlaying
            }

            ((oldState == State.PLAYING) && (newState == State.PAUSED)) -> {
                avdPlayingToPaused?.apply { start() } ?: drawablePaused
            }

            ((oldState == State.LOADING) && (newState == State.PAUSED)) -> {
                iconAppearAnimator.start()
                drawablePaused
            }

            ((oldState == State.LOADING) && (newState == State.PLAYING)) -> {
                iconAppearAnimator.start()
                drawablePlaying
            }

            else -> null
        }

        invalidate()
    }

    private fun updateAllDrawableBounds(): Unit = listOfNotNull(
        avdPausedToPlaying,
        avdPlayingToPaused,
        drawablePlaying,
        drawablePaused
    ).forEach { it.bounds = iconBounds }

    private fun cancelAllAnimations() {
        circlePulseAnimator.cancel()
        iconAppearAnimator.cancel()
        avdPausedToPlaying?.stop()
        avdPlayingToPaused?.stop()
    }

    fun showLoading() {
        state = State.LOADING
    }

    fun showPlaying() {
        state = State.PLAYING
    }

    fun showPaused() {
        state = State.PAUSED
    }

    enum class State {
        LOADING,
        PLAYING,
        PAUSED
    }

    private companion object {
        const val DEFAULT_CIRCLE_SCALE = 1f
        const val MIN_CIRCLE_SCALE = 0.8f
        const val DEFAULT_DRAWABLE_SCALE = 0.5f
        const val DEFAULT_ICON_SCALE = 1f
        const val MIN_ICON_SCALE = 0.5f

        const val ANIM_PULSE_DURATION = 1500L
        const val ANIM_RETURN_DURATION = 300L
        const val ANIM_ICON_POPUP_DURATION = 200L
    }

}
