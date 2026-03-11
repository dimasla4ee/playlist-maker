package com.dimasla4ee.playlistmaker.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography
import com.dimasla4ee.playlistmaker.app.ui.theme.appActionButtonColors

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = appActionButtonColors()
    ) {
        Text(
            modifier = Modifier.padding(AppDimensions.buttonContentPadding),
            text = text,
            style = AppTypography.labelLarge
        )
    }
}
