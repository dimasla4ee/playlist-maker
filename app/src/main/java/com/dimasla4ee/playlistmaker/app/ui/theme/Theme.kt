package com.dimasla4ee.playlistmaker.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    primaryContainer = LightBlue,
    onPrimaryContainer = DarkBlue,
    secondary = Gray,
    onSecondary = Black,
    tertiary = Red,
    onTertiary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = Black,
    inverseSurface = Black,
    inverseOnSurface = White,
    outline = Gray,
    outlineVariant = LightGray,
    error = DarkRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = White,
    primaryContainer = VeryDarkBlue,
    onPrimaryContainer = LightBlue,
    secondary = Gray,
    onSecondary = Black,
    tertiary = Red,
    onTertiary = White,
    background = Black,
    onBackground = White,
    surface = AlmostBlack,
    onSurface = White,
    surfaceVariant = DarkGray,
    onSurfaceVariant = Gray,
    inverseSurface = White,
    inverseOnSurface = Black,
    outline = MediumGray,
    outlineVariant = White,
    error = LightRed,
    onError = VeryDarkRed
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }

}
