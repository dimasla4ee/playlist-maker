package com.dimasla4ee.playlistmaker.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dimasla4ee.playlistmaker.creator.Creator
import com.dimasla4ee.playlistmaker.domain.use_case.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>()
    val isDarkThemeEnabled: LiveData<Boolean>
        get() = _isDarkThemeEnabled

    init {
        settingsInteractor.isDarkThemeEnabled(
            object : SettingsInteractor.SettingsConsumer {
                override fun consume(isDarkThemeEnabled: Boolean) {
                    _isDarkThemeEnabled.postValue(isDarkThemeEnabled)
                }
            }
        )
    }

    fun onThemeToggle(useDarkTheme: Boolean) {
        _isDarkThemeEnabled.postValue(useDarkTheme)
    }

    override fun onCleared() {
        super.onCleared()
        settingsInteractor.setAppTheme(_isDarkThemeEnabled.value ?: false)
    }

    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                SettingsViewModel(
                    Creator.provideSettingsInteractor(app)
                )
            }
        }
    }
}