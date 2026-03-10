@file:OptIn(ExperimentalMaterial3Api::class)

package com.dimasla4ee.playlistmaker.feature.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography
import com.dimasla4ee.playlistmaker.app.ui.theme.LocalAppColors
import com.dimasla4ee.playlistmaker.app.ui.theme.appSwitchColors

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
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = AppTypography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
    trailingIcon: @Composable (() -> Unit) = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimensions.settingsItemHeight)
            .clickable(onClick = onClick)
            .padding(horizontal = AppDimensions.paddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AppTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        trailingIcon()
    }
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
