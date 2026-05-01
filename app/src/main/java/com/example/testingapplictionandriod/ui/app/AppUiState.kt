package com.example.testingapplictionandriod.ui.app

sealed interface AppScreen {
    data object Splash : AppScreen
    data object Onboarding : AppScreen
    data object Main : AppScreen
}

data class AppUiState(
    val currentScreen: AppScreen = AppScreen.Splash,
    val onboardingStep: Int = 0,
    val selectedTab: MainTab = MainTab.CALENDAR
)

enum class MainTab { CALENDAR, TASKS, NOTES, MINE }
