package com.qubit.core_vhal.repository

import com.qubit.core_vhal.VhalParamsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class MockVhalReaderImpl @Inject constructor(): VhalReader {
    private val _carState = MutableStateFlow(VhalParamsState())
    override val carState: StateFlow<VhalParamsState> = _carState.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private var mockJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun connect() {
        mockJob?.cancel()
        mockJob = scope.launch {
            var currentRpm = 800
            var currentGear = 1

            while (isActive) {
                currentRpm += (100..300).random()
                if (currentRpm > 4000) {
                    currentRpm = 1500
                    if (currentGear < 6) currentGear++ else currentGear = 1
                }

                _carState.value = generateVhalParamsState(
                    gear = currentGear,
                    rpm = currentRpm,
                    isParkingBrakeOn = false
                )

                delay(500)
            }
        }
        _isConnected.value = true
    }

    override fun disconnect() {
        mockJob?.cancel()
        _isConnected.value = false
    }

    private fun generateVhalParamsState(
        gear: Int = 4,
        rpm: Int = 2500,
        vin: String = "TEST_VIN_777",
        make: String = "BMW",
        model: String = "530d",
        isParkingBrakeOn: Boolean = false,
        tirePressures: Map<Int, Float> = mapOf(1 to 2.4f, 2 to 2.4f, 4 to 2.2f, 8 to 2.2f)
    ): VhalParamsState {
        val state = VhalParamsState(
            gear = gear,
            isParkingBrakeOn = isParkingBrakeOn,
            rpm = rpm,
            tirePressures = tirePressures,
            vin = vin,
            make = make,
            model = model,
        )
        return state
    }
}