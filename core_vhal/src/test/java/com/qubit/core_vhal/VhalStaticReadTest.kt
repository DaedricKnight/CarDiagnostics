package com.qubit.core_vhal

import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class VhalStaticReadTest {

    @Test
    fun `readStaticValue - INFO_VIN updates state flow correctly`()  = runTest {
        val stateFlow = MutableStateFlow(VhalParamsState())
        val mockManager = mockk<CarPropertyManager>()
        val vinValue = "TEST_VIN_12345"

        val mockProp = mockk<CarPropertyValue<Any>>()
        every { mockProp.value } returns vinValue
        every { mockManager.getProperty<Any>(VehiclePropertyIds.INFO_VIN, 0) } returns mockProp

        readStaticValue(mockManager, VehiclePropertyIds.INFO_VIN, stateFlow)

        assertEquals(vinValue, stateFlow.value.vin)
    }

    @Test
    fun `readStaticValue - on exception does not crash and keeps state`() = runTest {
        mockkStatic(android.util.Log::class)
        every { android.util.Log.w(any(), any<String>()) } returns 0

        val stateFlow = MutableStateFlow(VhalParamsState(vin = "OLD_VIN",))
        val mockManager = mockk<CarPropertyManager>()

        every {
            mockManager.getProperty<Any>(any(), any())
        } throws SecurityException("No permission")

        readStaticValue(mockManager, VehiclePropertyIds.INFO_VIN, stateFlow)

        assertEquals("OLD_VIN", stateFlow.value.vin)

        verify { android.util.Log.w("VHAL", any<String>()) }

        unmockkStatic(android.util.Log::class)
    }
}