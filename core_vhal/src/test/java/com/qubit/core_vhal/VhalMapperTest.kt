package com.qubit.core_vhal

import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class VhalMapperTest {

    private val initialState = VhalParamsState()

    @Test
    fun `mapPropertyToState - GEAR_SELECTION updates gear correctly`() {
        val mockValue = mockk<CarPropertyValue<Int>>()
        every { mockValue.propertyId } returns VehiclePropertyIds.GEAR_SELECTION
        every { mockValue.value } returns 4 // Drive

        val result = mapPropertyToState(mockValue, initialState)

        assertEquals(4, result.gear)
    }

    @Test
    fun `mapPropertyToState - ENGINE_RPM converts float to int correctly`() {
        val mockValue = mockk<CarPropertyValue<Float>>()
        every { mockValue.propertyId } returns VehiclePropertyIds.ENGINE_RPM
        every { mockValue.value } returns 2500.7f

        val result = mapPropertyToState(mockValue, initialState)

        assertEquals(2500, result.rpm)
    }

    @Test
    fun `mapPropertyToState - TIRE_PRESSURE updates specific wheel area`() {
        val leftFrontWheel = 1
        val pressureValue = 2.4f

        val mockValue = mockk<CarPropertyValue<Float>>()
        every { mockValue.propertyId } returns VehiclePropertyIds.TIRE_PRESSURE
        every { mockValue.areaId } returns leftFrontWheel
        every { mockValue.value } returns pressureValue

        val firstResult = mapPropertyToState(mockValue, initialState)

        val rightFrontWheel = 2
        val mockValue2 = mockk<CarPropertyValue<Float>>()
        every { mockValue2.propertyId } returns VehiclePropertyIds.TIRE_PRESSURE
        every { mockValue2.areaId } returns rightFrontWheel
        every { mockValue2.value } returns 2.2f

        val finalResult = mapPropertyToState(mockValue2, firstResult)

        assertEquals(2.4f, finalResult.tirePressures[1])
        assertEquals(2.2f, finalResult.tirePressures[2])
        assertEquals(2, finalResult.tirePressures.size)
    }

    @Test
    fun `mapPropertyToState - unknown propertyId returns current state`() {
        val mockValue = mockk<CarPropertyValue<Int>>()
        every { mockValue.propertyId } returns -1 // ID is not exist

        val result = mapPropertyToState(mockValue, initialState)

        assertEquals(initialState, result)
    }
}