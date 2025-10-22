package com.dimasla4ee.playlistmaker.core.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.databinding.ViewPanelHeaderBinding

@Suppress("unused")
class PanelHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPanelHeaderBinding

    init {
        orientation = HORIZONTAL
        binding = ViewPanelHeaderBinding.inflate(LayoutInflater.from(context), this)

        context.theme.obtainStyledAttributes(
            attrs, R.styleable.PanelHeader, defStyleAttr, 0
        ).apply {
            try {
                binding.titleTextView.apply {
                    text = getString(R.styleable.PanelHeader_android_text)

                    setTextColor(
                        getColor(
                            R.styleable.PanelHeader_android_textColor, currentTextColor
                        )
                    )

                    if (hasValue(R.styleable.PanelHeader_android_textSize)) {
                        textSize = getDimension(
                            R.styleable.PanelHeader_android_textSize, textSize
                        )
                    }
                }

                binding.iconButton.apply {
                    val showIcon = getBoolean(R.styleable.PanelHeader_showIcon, false)
                    isIconVisible = showIcon

                    if (hasValue(R.styleable.PanelHeader_iconSrc)) {
                        setImageResource(getResourceId(R.styleable.PanelHeader_iconSrc, 0))
                    }

                    if (hasValue(R.styleable.PanelHeader_iconTint)) {
                        imageTintList = getColorStateList(R.styleable.PanelHeader_iconTint)
                    }
                }

            } finally {
                recycle()
            }
        }
    }

    var text: CharSequence
        get() = binding.titleTextView.text
        set(value) {
            binding.titleTextView.text = text
        }

    fun setOnIconClickListener(listener: OnClickListener) {
        binding.iconButton.setOnClickListener(listener)
    }

    var isIconVisible: Boolean
        get() = binding.iconButton.isVisible
        set(value) {
            binding.iconButton.visibility = if (value) VISIBLE else GONE
        }
}