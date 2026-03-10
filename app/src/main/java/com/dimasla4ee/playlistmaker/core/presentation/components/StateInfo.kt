package com.dimasla4ee.playlistmaker.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography

@Composable
fun StateInfo(
    text: String,
    @DrawableRes drawable: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null
        )
        Spacer(Modifier.height(AppDimensions.paddingMedium))
        Text(
            text = text,
            maxLines = 4,
            style = AppTypography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
