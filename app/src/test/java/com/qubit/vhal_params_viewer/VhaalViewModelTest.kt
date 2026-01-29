package com.qubit.vhal_params_viewer

import app.cash.turbine.test
import com.qubit.core_vhal.VhalParamsState
import com.qubit.core_vhal.repository.VhalReader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CarDiagnosticsViewModel
    private val mockVhalManager = mockk<VhalReader>(relaxed = true)

    private val fakeCarState = MutableStateFlow(VhalParamsState())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockVhalManager.carState } returns fakeCarState

        viewModel = CarDiagnosticsViewModel(mockVhalManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should reflect changes from vhalManager`() = runTest {
        val movingState = VhalParamsState(
            gear = 4,
            rpm = 3000,,
        )

        fakeCarState.value = movingState

        assertEquals(3000, viewModel.uiState.value.rpm)
        assertEquals(4, viewModel.uiState.value.gear)
    }

    @Test
    fun `connect should trigger manager connection`() {
        viewModel.connect()
        verify { mockVhalManager.connect() }
    }

    @Test
    fun `formatting check - speed display logic`() = runTest {

        val state = VhalParamsState(rpm = 4000,)
        fakeCarState.value = state

        assertEquals(4000, viewModel.uiState.value.rpm)
    }

    @Test
    fun `uiState should emit multiple updates`() = runTest {
        viewModel.uiState.test {
            assertEquals(0, awaitItem().rpm)

            fakeCarState.value = VhalParamsState(rpm = 1000,)
            assertEquals(1000, awaitItem().rpm)

            fakeCarState.value = VhalParamsState(rpm = 2000,)
            assertEquals(2000, awaitItem().rpm)
        }
    }
}