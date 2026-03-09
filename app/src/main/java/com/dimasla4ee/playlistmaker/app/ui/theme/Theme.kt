package com.dimasla4ee.playlistmaker.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3772E7),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD9E6FF),
    onPrimaryContainer = Color(0xFF00183A),
    secondary = Color(0xFFAEAFB4),
    onSecondary = Color(0xFF1A1B22),
    tertiary = Color(0xFFF56B6C),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1B22),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1B22),
    surfaceVariant = Color(0xFFE6E8EB),
    onSurfaceVariant = Color(0xFF1A1B22),
    inverseSurface = Color(0xFF1A1B22),
    inverseOnSurface = Color(0xFFFFFFFF),
    outline = Color(0xFFAEAFB4),
    outlineVariant = Color(0xFFE6E8EB),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3772E7),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF0B2B66),
    onPrimaryContainer = Color(0xFFD9E6FF),
    secondary = Color(0xFFAEAFB4),
    onSecondary = Color(0xFF1A1B22),
    tertiary = Color(0xFFF56B6C),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF1A1B22),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF121214),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2A2B2E),
    onSurfaceVariant = Color(0xFFAEAFB4),
    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF1A1B22),
    outline = Color(0xFF3B3C3F),
    outlineVariant = Color(0xFFFFFFFF),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!darkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
