package com.qubit.core_vhal

import android.car.Car
import android.car.VehiclePropertyIds
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
        car = Car.createCar(context, null, Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER) { car, ready ->
            if (ready) {
                propertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as? CarPropertyManager
                registerProperties()
            }
        }
    }

    private fun registerProperties() {
        val manager = propertyManager ?: return

        val availableConfigs = manager.propertyList
        val availableIds = availableConfigs.map { it.propertyId }.toSet()

        val propertiesToSubscribe = intArrayOf(
            // Main driving data
            VehiclePropertyIds.GEAR_SELECTION,
            VehiclePropertyIds.PARKING_BRAKE_ON,
            VehiclePropertyIds.FUEL_LEVEL,
            VehiclePropertyIds.INFO_FUEL_CAPACITY,
            VehiclePropertyIds.FUEL_LEVEL_LOW,
            VehiclePropertyIds.ENGINE_OIL_LEVEL,
            VehiclePropertyIds.RANGE_REMAINING,
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE,


            // --- ENGINE AND MAINTENANCE PARAMETERS ---
            VehiclePropertyIds.ENGINE_COOLANT_TEMP,     // Coolant temperature (overheating monitoring)
            VehiclePropertyIds.ENGINE_OIL_TEMP,         // Oil temperature (load monitoring)
            VehiclePropertyIds.ENGINE_RPM,              // Engine revolutions per minute (idle check)
            VehiclePropertyIds.IGNITION_STATE,          // Ignition system status
            VehiclePropertyIds.PERF_ODOMETER,           // Total distance traveled (maintenance tracking)

            // --- SAFETY SYSTEMS ---
            VehiclePropertyIds.ABS_ACTIVE,              // ABS engagement (sensor/system health)
            VehiclePropertyIds.TRACTION_CONTROL_ACTIVE, // Traction control system status
            VehiclePropertyIds.TIRE_PRESSURE,           // Tire pressure monitoring (per wheel)
            VehiclePropertyIds.CRITICALLY_LOW_TIRE_PRESSURE, // Critical tire pressure warning threshold

            // --- ELECTRICAL AND BODY ---
            VehiclePropertyIds.EV_BATTERY_LEVEL,        // Battery charge level (for EV/Hybrid models)
            VehiclePropertyIds.EV_CHARGE_PORT_CONNECTED,// Charging port connection status
            VehiclePropertyIds.DOOR_LOCK,               // Central locking system status

            // --- LIGHTS ---
            VehiclePropertyIds.TURN_SIGNAL_STATE,
            VehiclePropertyIds.HEADLIGHTS_STATE,
            VehiclePropertyIds.HIGH_BEAM_LIGHTS_STATE,
            VehiclePropertyIds.NIGHT_MODE,

            // --- EV CHARGE ---
            VehiclePropertyIds.EV_CHARGE_STATE,
            VehiclePropertyIds.EV_CHARGE_TIME_REMAINING,
            VehiclePropertyIds.EV_REGENERATIVE_BRAKING_STATE,
            VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY,

            // --- INFO ---
            VehiclePropertyIds.INFO_VIN,
            VehiclePropertyIds.INFO_FUEL_TYPE,
            VehiclePropertyIds.INFO_MAKE,
            VehiclePropertyIds.INFO_MODEL,
            VehiclePropertyIds.INFO_MODEL_YEAR,

            // --- OBD2 SPECIFICATION ---
            VehiclePropertyIds.OBD2_LIVE_FRAME,         // Real-time OBD diagnostic data stream
            VehiclePropertyIds.OBD2_FREEZE_FRAME        // Snapshot of data stored when a fault occurs
        )

        propertiesToSubscribe.forEach { id ->
            if (availableIds.contains(id)) {
                manager.registerCallback(
                    propertyCallback,
                    id,
                    CarPropertyManager.SENSOR_RATE_ONCHANGE
                )
            } else {
                Log.w("VHAL_LIB", "Property ID $id is not supported by this vehicle")
            }
        }
    }

    override fun disconnect() {
        propertyManager?.unregisterCallback(propertyCallback)
        car?.disconnect()
    }
}