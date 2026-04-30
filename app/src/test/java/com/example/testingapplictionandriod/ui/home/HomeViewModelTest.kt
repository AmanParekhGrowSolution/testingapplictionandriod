package com.example.testingapplictionandriod.ui.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has isLoading false`() = runTest {
        val viewModel = HomeViewModel()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
    }

    @Test
    fun `uiState emits initial HomeUiState`() = runTest {
        val viewModel = HomeViewModel()

        val state = viewModel.uiState.value

        assert(state == HomeUiState())
    }
}
