package com.qubit.core_vhal

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyConfig
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VhalReaderImpl(private val context: Context): VhalReader {
    private var car: Car? = null
    private var propertyManager: CarPropertyManager? = null

    private val _carState = MutableStateFlow(VhalParamsState.initial)
    override val carState: StateFlow<VhalParamsState> = _carState.asStateFlow()

    private val propertyCallback = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(value: CarPropertyValue<*>) {
            _carState.update { currentState ->
                mapPropertyToState(value, currentState)
            }
        }

        override fun onErrorEvent(propId: Int, area: Int) {
            Log.e("VHAL_LIB", "Error on property $propId in area $area")
        }
    }

    override fun connect() {
        car = Car.createCar(context, null, Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER) { carInstance, ready ->
            if (ready) {
                propertyManager = carInstance.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
                registerSensors()
            }
        }
    }

    private fun registerSensors() {
        val manager = propertyManager ?: return
        val configs = manager.propertyList

        configs.forEach { config ->
            val id = config.propertyId

            if (config.changeMode != CarPropertyConfig.VEHICLE_PROPERTY_CHANGE_MODE_STATIC) {
                try {
                    manager.registerCallback(
                        propertyCallback,
                        id,
                        CarPropertyManager.SENSOR_RATE_ONCHANGE
                    )
                } catch (e: SecurityException) {
                    Log.e("VHAL", "No permission to register property: $id")
                }
            } else {
                readStaticValue(id)
            }
        }
    }

    private fun readStaticValue(id: Int) {
        val manager = propertyManager ?: return
        try {
            val value = manager.getProperty<Any>(id, 0).value
            _carState.update { currentState ->
                when (id) {
                    VehiclePropertyIds.INFO_VIN -> currentState.copy(vin = value as String)
                    VehiclePropertyIds.INFO_FUEL_TYPE -> currentState.copy(fuelType = value as Array<Int>)
                    VehiclePropertyIds.INFO_MAKE -> currentState.copy(make = value as String)
                    VehiclePropertyIds.INFO_MODEL -> currentState.copy(model = value as String)
                    VehiclePropertyIds.INFO_MODEL_YEAR -> currentState.copy(modelYear = value as Int)
                    VehiclePropertyIds.INFO_FUEL_CAPACITY -> currentState.copy(fuelCapacity = value as Float)
                    VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY -> currentState.copy(evBatteryCapacity = value as Float)
                    else -> currentState
                }
            }
        } catch (e: Exception) {
            Log.w("VHAL", "Could not read static property $id, error: $e")
        }
    }

    override fun disconnect() {
        propertyManager?.unregisterCallback(propertyCallback)
        car?.disconnect()
    }
}