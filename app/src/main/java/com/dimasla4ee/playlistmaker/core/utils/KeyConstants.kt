package com.dimasla4ee.playlistmaker.core.utils

object KeyConstants {

    private const val APP_PACKAGE = "io.dimasla4ee.playlistmaker"

    const val APP_PREFERENCES = "app_preferences"
    const val SEARCH_PREFERENCES = "search_preferences"

    const val SOURCE_URL = "${APP_PACKAGE}.SOURCE_URL"
    const val SOURCE_ARTIST_TITLE = "${APP_PACKAGE}.SOURCE_ARTIST_TITLE"

    object Preference {
        const val DARK_THEME = "dark_theme_enabled"
        const val SEARCH_HISTORY = "search_history_songs"
    }

}