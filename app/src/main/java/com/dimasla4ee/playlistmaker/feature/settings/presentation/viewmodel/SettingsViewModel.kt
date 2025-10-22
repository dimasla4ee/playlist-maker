package com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>()
    val isDarkThemeEnabled: LiveData<Boolean>
        get() = _isDarkThemeEnabled

    init {
        val isDarkThemeEnabled = settingsInteractor.isDarkThemeEnabled()
        _isDarkThemeEnabled.postValue(isDarkThemeEnabled)
    }

    fun onThemeToggle(useDarkTheme: Boolean) {
        _isDarkThemeEnabled.postValue(useDarkTheme)
    }

    override fun onCleared() {
        super.onCleared()
        settingsInteractor.setAppTheme(_isDarkThemeEnabled.value ?: false)
    }
}