package com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    val isDarkThemeEnabled: StateFlow<Boolean>
        field = MutableStateFlow(false)

    init {
        val isDarkTheme = settingsInteractor.isDarkThemeEnabled()
        isDarkThemeEnabled.update { isDarkTheme }
    }

    fun onThemeToggle(useDarkTheme: Boolean) {
        isDarkThemeEnabled.update { useDarkTheme }
    }

    fun onPause() {
        settingsInteractor.setAppTheme(isDarkThemeEnabled.value)
    }

    override fun onCleared() {
        super.onCleared()
        settingsInteractor.setAppTheme(isDarkThemeEnabled.value)
    }
}