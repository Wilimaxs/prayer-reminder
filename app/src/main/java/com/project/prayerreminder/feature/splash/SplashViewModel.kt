package com.project.prayerreminder.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateProgress()
    }

    fun updateProgress() {
        viewModelScope.launch {
            for (i in 0..100) {
                _uiState.value = _uiState.value.copy(currentProgress = i / 100f)
                delay(100)
            }
        }
    }
}