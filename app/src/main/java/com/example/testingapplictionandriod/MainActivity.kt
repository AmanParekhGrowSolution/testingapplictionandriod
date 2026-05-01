package com.example.testingapplictionandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testingapplictionandriod.ui.app.AppScreen
import com.example.testingapplictionandriod.ui.app.AppViewModel
import com.example.testingapplictionandriod.ui.main.MainScreen
import com.example.testingapplictionandriod.ui.onboarding.OnboardingScreen
import com.example.testingapplictionandriod.ui.splash.SplashScreen
import com.example.testingapplictionandriod.ui.theme.TestingapplictionandriodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingapplictionandriodTheme {
                val appViewModel: AppViewModel = viewModel()
                val appState = appViewModel.uiState.collectAsStateWithLifecycle()

                when (val screen = appState.value.currentScreen) {
                    is AppScreen.Splash -> {
                        val context = this
                        SplashScreen(
                            onSplashDone = { appViewModel.onSplashComplete(context) }
                        )
                    }
                    is AppScreen.Onboarding -> {
                        val context = this
                        OnboardingScreen(
                            step = appState.value.onboardingStep,
                            onNext = appViewModel::onOnboardingNext,
                            onGetStarted = { appViewModel.onOnboardingComplete(context) }
                        )
                    }
                    is AppScreen.Main -> {
                        MainScreen(appViewModel = appViewModel)
                    }
                }
            }
        }
    }
}
