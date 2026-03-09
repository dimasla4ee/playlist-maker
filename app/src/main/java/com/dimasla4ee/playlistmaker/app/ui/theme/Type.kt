package com.dimasla4ee.playlistmaker.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dimasla4ee.playlistmaker.R


val YsDisplayFontFamily = FontFamily(
    Font(R.font.ys_display_regular, weight = FontWeight.Normal),
    Font(R.font.ys_display_medium, weight = FontWeight.Medium)
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 28.sp,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        fontSize = 16.sp
    )
)