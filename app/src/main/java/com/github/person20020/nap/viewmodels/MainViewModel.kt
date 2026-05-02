package com.github.person20020.nap.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.person20020.nap.data.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val prefs: PreferencesRepository,
) : ViewModel() {
    val darkTheme: StateFlow<Int> =
        prefs.darkTheme
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = 0)
    val dynamicColors: StateFlow<Boolean> =
        prefs.dynamicColors
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = false)
    val seedColor: StateFlow<Long> =
        prefs.seedColor
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = 0L)

    val developerSettings: StateFlow<Boolean> =
        prefs.developerSettings
            .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = false)

    fun setDarkTheme(value: Int) {
        viewModelScope.launch {
            prefs.setDarkTheme(value)
        }
    }

    fun setDynamicColors(value: Boolean) {
        viewModelScope.launch {
            prefs.setDynamicColors(value)
        }
    }

    fun setSeedColor(value: Long) {
        viewModelScope.launch {
            prefs.setSeedColor(value)
        }
    }

    fun setDeveloperSettings(value: Boolean) {
        viewModelScope.launch {
            prefs.setDeveloperSettings(value)
        }
    }

    companion object {
        fun factory(prefs: PreferencesRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { MainViewModel(prefs) }
            }
    }
}
