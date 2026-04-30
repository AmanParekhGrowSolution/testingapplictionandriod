package com.example.testingapplictionandriod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testingapplictionandriod.ui.home.HomeScreen
import com.example.testingapplictionandriod.ui.home.HomeViewModel
import com.example.testingapplictionandriod.ui.theme.TestingapplictionandriodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingapplictionandriodTheme {
                val viewModel: HomeViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(uiState = uiState)
            }
        }
    }
}
