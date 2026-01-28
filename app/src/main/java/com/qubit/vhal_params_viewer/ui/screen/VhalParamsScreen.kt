package com.qubit.vhal_params_viewer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qubit.vhal_params_viewer.CarDiagnosticsViewModel

@Composable
fun VhalParamsScreen(viewModel: CarDiagnosticsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Header("Info") }
        item { ParamRow("VIN", state.vin) }
        item { ParamRow("Fuel type", state.fuelType.toList().toString()) }
        item { ParamRow("Make", state.make) }
        item { ParamRow("Model", state.model) }
        item { ParamRow("Model year", state.modelYear.toString()) }

        item { Header("Main Driving Data") }
        item { ParamRow("Gear (Raw)", state.gear.toString()) }
        item { ParamRow("Parking Brake", state.isParkingBrakeOn.toString(), isAlert = state.isParkingBrakeOn) }
        item { ParamRow("Odometer", "${state.odometer} km") }
        item { ParamRow("RPM", state.rpm.toString()) }
        item { ParamRow("Range", state.range.toString()) }
        item { ParamRow("Outside temperature", state.outsideTemp.toString()) }

        item { Header("Fuel & Energy") }
        item { ParamRow("Fuel Level", "${state.fuelLevel} ml") }
        item { ParamRow("Fuel Capacity", "${state.fuelCapacity} L") }
        item { ParamRow("Low Fuel", state.isFuelLow.toString(), isAlert = state.isFuelLow) }
        item { ParamRow("EV Battery", "${state.evBatteryLevel} w/h") }
        item { ParamRow("Charge Connected", state.isEvChargePortConnected.toString()) }

        item { Header("Engine & Maintenance") }
        item { ParamRow("Coolant Temp", "${state.coolantTemp} °C", isAlert = state.coolantTemp > 105) }
        item { ParamRow("Oil Temp", "${state.oilTemp} °C", isAlert = state.oilTemp > 120) }
        item { ParamRow("Oil Level", state.oilLevel.toString()) }
        item { ParamRow("Ignition State", state.ignitionState.toString()) }

        item { Header("Safety & Chassis") }
        item { ParamRow("ABS Active", state.isAbsActive.toString(), isAlert = !state.isAbsActive) }
        item { ParamRow("Traction Active", state.isTractionControlActive.toString(), isAlert = !state.isTractionControlActive) }
        item { ParamRow("Door Locked", state.isDoorLocked.toString()) }

        item { Header("Lights") }
        item { ParamRow("TURN_SIGNAL_STATE", state.turnSignal.toString()) }
        item { ParamRow("HEADLIGHTS_STATE", state.headLightState.toString()) }
        item { ParamRow("HIGH_BEAM_LIGHTS_STATE", state.highBeamState.toString()) }
        item { ParamRow("NIGHT_MODE", state.nighMode.toString()) }

        item { Header("EV charge") }
        item { ParamRow("EV_CHARGE_STATE", state.evCharge.toString()) }
        item { ParamRow("EV_CHARGE_TIME_REMAINING", state.evChargeTime.toString()) }
        item { ParamRow("EV_REGENERATIVE_BRAKING_STATE", state.isEvRecuperate.toString()) }
        item { ParamRow("EV Battery Capacity", state.evBatteryCapacity.toString()) }

        // Dynamic Tire Pressure Rows
        state.tirePressures.forEach { (area, pressure) ->
            item { ParamRow("Tire Pressure (Area $area)", "$pressure psi", isAlert = state.isCriticallyLowTirePressure) }
        }

        item { Header("Diagnostic & OBD") }
        item { ParamRow("Check Engine", state.isCheckEngineOn.toString(), isAlert = state.isCheckEngineOn) }
        item { ParamRow("OBD Data", state.obdData.ifBlank { "No Data" }) }
        item { ParamRow("Active DTCs", state.activeDTCs.joinToString().ifBlank { "None" }) }
    }
}

@Composable
fun Header(text: String) {
    Text(
        text = text.uppercase(),
        style = TextStyle(
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}

@Composable
fun ParamRow(label: String, value: String, isAlert: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(4.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White)
        Text(
            text = value,
            color = if (isAlert) Color.Red else Color.Cyan,
            fontWeight = FontWeight.Medium
        )
    }
}