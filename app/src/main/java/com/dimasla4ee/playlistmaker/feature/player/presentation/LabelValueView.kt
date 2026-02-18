package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.dimasla4ee.playlistmaker.R
import com.google.android.material.textview.MaterialTextView

class LabelValueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val labelTextView: MaterialTextView
    private val valueTextView: MaterialTextView

    init {
        inflate(context, R.layout.label_value_item, this)
        labelTextView = findViewById(R.id.label)
        valueTextView = findViewById(R.id.value)

        context.obtainStyledAttributes(
            attrs,
            R.styleable.LabelValueView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                labelTextView.apply {
                    text = getString(R.styleable.LabelValueView_labelText)
                    setWeight(getFloat(R.styleable.LabelValueView_labelWeight, 3f))
                }
                valueTextView.apply {
                    text = getString(R.styleable.LabelValueView_valueText)
                    setWeight(getFloat(R.styleable.LabelValueView_valueWeight, 7f))
                }
            } finally {
                recycle()
            }
        }
    }

    fun setLabel(label: String) {
        labelTextView.text = label
    }
    fun setValue(value: String) {
        valueTextView.text = value
    }

    fun setLabelWeight(weight: Float) = labelTextView.setWeight(weight)
    fun setValueWeight(weight: Float) = valueTextView.setWeight(weight)

    private fun MaterialTextView.setWeight(weight: Float) {
        layoutParams = (layoutParams as LayoutParams).apply {
            horizontalWeight = weight
        }
    }

}