package com.qubit.core_vhal

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyConfig
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VhalManagerTest {
    private lateinit var vhalManager: VhalReaderImpl
    private val mockContext = mockk<Context>(relaxed = true)

    @Before
    fun setup() {
        vhalManager = VhalReaderImpl(mockContext)

        mockkStatic(android.util.Log::class)
        every { android.util.Log.e(any(), any()) } returns 0
        every { android.util.Log.w(any<String>(), any<String>()) } returns 0
    }

    @Test
    fun `propertyCallback updates state when onChangeEvent triggered`() = runTest {
        val field = vhalManager.javaClass.getDeclaredField("propertyCallback")
        field.isAccessible = true
        val callback = field.get(vhalManager) as CarPropertyManager.CarPropertyEventCallback

        val mockValue = mockk<CarPropertyValue<Int>>()
        every { mockValue.propertyId } returns VehiclePropertyIds.GEAR_SELECTION
        every { mockValue.value } returns 4 // Состояние Drive

        callback.onChangeEvent(mockValue)

        assertEquals(4, vhalManager.carState.value.gear)
    }

    @Test
    fun `connect - successful initialization calls registerSensors`() {
        mockkStatic(Car::class)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        val mockCar = mockk<Car>(relaxed = true)
        val mockPropManager = mockk<CarPropertyManager>(relaxed = true)

        val carListenerSlot = slot<Car.CarServiceLifecycleListener>()

        every {
            Car.createCar(any(), any(), any(), capture(carListenerSlot))
        } returns mockCar

        every { mockCar.getCarManager(Car.PROPERTY_SERVICE) } returns mockPropManager

        vhalManager.connect()

        carListenerSlot.captured.onLifecycleChanged(mockCar, true)

        verify { mockCar.getCarManager(Car.PROPERTY_SERVICE) }
        verify { mockPropManager.propertyList } // registerSensors вызывает это
    }

    @Test
    fun `registerSensors - distinguishes between static and dynamic properties`() {
        val mockManager = mockk<CarPropertyManager>(relaxed = true)

        val dynamicConfig = mockk<CarPropertyConfig<Int>>()
        every { dynamicConfig.propertyId } returns VehiclePropertyIds.ENGINE_RPM
        every { dynamicConfig.changeMode } returns CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_ONCHANGE

        val staticConfig = mockk<CarPropertyConfig<Int>>()
        every { staticConfig.propertyId } returns VehiclePropertyIds.INFO_VIN
        every { staticConfig.changeMode } returns CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC

        every { mockManager.propertyList } returns listOf(dynamicConfig, staticConfig)

        vhalManager.setProperty("propertyManager", mockManager)

        vhalManager.invokePrivateMethod<Unit>("registerSensors")

        verify {
            mockManager.registerCallback(any(), VehiclePropertyIds.ENGINE_RPM, any())
        }
        verify(exactly = 0) {
            mockManager.registerCallback(any(), VehiclePropertyIds.INFO_VIN, any())
        }
    }

    @Test
    fun `disconnect - unregisters callback and disconnects car`() {
        val mockCar = mockk<Car>(relaxed = true)
        val mockManager = mockk<CarPropertyManager>(relaxed = true)

        vhalManager.setProperty("car", mockCar)
        vhalManager.setProperty("propertyManager", mockManager)

        vhalManager.disconnect()

        verify { mockManager.unregisterCallback(any<CarPropertyManager.CarPropertyEventCallback>()) }
        verify { mockCar.disconnect() }
    }
}


fun Any.setProperty(propertyName: String, value: Any?) {
    val field = this.javaClass.getDeclaredField(propertyName)
    field.isAccessible = true
    field.set(this, value)
}

fun Any.getProperty(propertyName: String): Any? {
    val field = this.javaClass.getDeclaredField(propertyName)
    field.isAccessible = true
    return field.get(this)
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.invokePrivateMethod(methodName: String, vararg args: Any?): T? {
    val argTypes = args.map { it!!::class.javaPrimitiveType ?: it::class.java }.toTypedArray()

    val method = this.javaClass.getDeclaredMethod(methodName, *argTypes)
    method.isAccessible = true
    return method.invoke(this, *args) as T?
}