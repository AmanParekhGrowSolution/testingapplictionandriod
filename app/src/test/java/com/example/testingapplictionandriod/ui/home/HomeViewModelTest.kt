package com.example.testingapplictionandriod.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun `initial count is zero`() {
        val viewModel = HomeViewModel()
        assertEquals(0, viewModel.uiState.value.count)
    }

    @Test
    fun `count increments on each tap`() {
        val viewModel = HomeViewModel()
        viewModel.onTap()
        assertEquals(1, viewModel.uiState.value.count)
        viewModel.onTap()
        assertEquals(2, viewModel.uiState.value.count)
    }
}
