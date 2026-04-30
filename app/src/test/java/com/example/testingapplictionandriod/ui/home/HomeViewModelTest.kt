package com.example.testingapplictionandriod.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun initialGreetingValue() {
        val viewModel = HomeViewModel()
        assertEquals("Hello aman", viewModel.uiState.value.greeting)
    }
}
