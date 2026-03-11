@file:OptIn(ExperimentalMaterial3Api::class)

package com.dimasla4ee.playlistmaker.feature.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography
import com.dimasla4ee.playlistmaker.app.ui.theme.LocalAppColors
import com.dimasla4ee.playlistmaker.app.ui.theme.appSwitchColors
import com.dimasla4ee.playlistmaker.core.presentation.components.RowListItem
import com.dimasla4ee.playlistmaker.core.presentation.components.TitleAppBar

@Composable
fun SettingsPane(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onShareClick: () -> Unit,
    onSupportClick: () -> Unit,
    onAgreementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    data class SettingsItem(
        val titleRes: Int,
        val onClick: () -> Unit,
        val iconRes: Int
    )

    val items = remember {
        listOf(
            SettingsItem(R.string.share_app, onShareClick, R.drawable.ic_share_24),
            SettingsItem(R.string.text_support, onSupportClick, R.drawable.ic_support_24),
            SettingsItem(R.string.user_agreement, onAgreementClick, R.drawable.ic_arrow_forward_24)
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TitleAppBar(
                title = stringResource(R.string.settings),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SettingItem(
                title = stringResource(R.string.dark_theme),
                onClick = { onThemeToggle(!isDarkTheme) }
            ) {
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = null,
                    colors = appSwitchColors()
                )
            }

            items.forEach { item ->
                SettingItem(
                    title = stringResource(item.titleRes),
                    onClick = item.onClick
                ) {
                    Icon(
                        painter = painterResource(item.iconRes),
                        tint = LocalAppColors.current.settingDrawable,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    trailingContent: @Composable (() -> Unit) = {}
) {
    RowListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(AppDimensions.rowListItemHeight)
            .padding(horizontal = AppDimensions.paddingMedium),
        headlineContent = {
            Text(
                text = title,
                style = AppTypography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        trailingContent = trailingContent
    )
}

@Preview(showSystemUi = true)
@Composable
fun SettingsPanePreview() {
    SettingsPane(
        isDarkTheme = true,
        onThemeToggle = {},
        onShareClick = {},
        onSupportClick = {},
        onAgreementClick = { }
    )
}
