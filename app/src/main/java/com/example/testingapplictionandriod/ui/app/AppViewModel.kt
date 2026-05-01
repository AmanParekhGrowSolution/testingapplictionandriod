package com.example.testingapplictionandriod.ui.app

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val PREFS_NAME = "calenderly_prefs"
private const val KEY_ONBOARDING_DONE = "onboarding_done"

class AppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun onSplashComplete(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        _uiState.update {
            it.copy(currentScreen = if (onboardingDone) AppScreen.Main else AppScreen.Onboarding)
        }
    }

    fun onOnboardingNext() {
        val step = _uiState.value.onboardingStep
        if (step < 1) {
            _uiState.update { it.copy(onboardingStep = step + 1) }
        }
    }

    fun onOnboardingComplete(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ONBOARDING_DONE, true).apply()
        _uiState.update { it.copy(currentScreen = AppScreen.Main) }
    }

    fun onTabSelected(tab: MainTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
