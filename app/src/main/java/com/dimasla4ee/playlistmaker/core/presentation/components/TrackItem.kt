package com.dimasla4ee.playlistmaker.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography
import com.dimasla4ee.playlistmaker.app.ui.theme.LocalAppColors

@Composable
fun TrackItem(
    title: String,
    author: String,
    duration: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(AppDimensions.settingsItemHeight)
            .padding(horizontal = AppDimensions.paddingSmall),
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(2.dp)),
                model = imageUrl,
                placeholder = painterResource(R.drawable.ic_placeholder_45),
                error = painterResource(R.drawable.ic_placeholder_45),
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                modifier = Modifier.padding(horizontal = AppDimensions.paddingSmall),
                text = title,
                style = AppTypography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                modifier = Modifier.padding(horizontal = AppDimensions.paddingSmall),
                text = "$author · $duration",
                style = AppTypography.labelSmall,
                color = colorResource(R.color.cardLowerText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward_24),
                tint = LocalAppColors.current.settingDrawable,
                contentDescription = null
            )
        }
    )
}
