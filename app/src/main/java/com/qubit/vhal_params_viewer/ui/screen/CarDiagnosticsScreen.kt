package com.qubit.vhal_params_viewer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qubit.core_vhal.VhalParamsState
import com.qubit.vhal_params_viewer.viewmodel.CarDiagnosticsViewModel
import com.qubit.vhal_params_viewer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VhalParamsScreen(viewModel: CarDiagnosticsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isMockMode by viewModel.isMockMode.collectAsStateWithLifecycle()
    TopBar(
        checked = isMockMode,
        onCheckedChange = { viewModel.toggleVhalSource(it) },
        content = { padding ->
            VhalDataContent(modifier = Modifier.padding(padding), uiState = uiState)
        })
}

@Composable
fun VhalDataContent(modifier: Modifier, uiState: VhalParamsState) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Header(stringResource(R.string.header_info)) }
        item { ParamRow(stringResource(R.string.param_vin), uiState.vin) }
        item {
            ParamRow(
                stringResource(R.string.param_fuel_type),
                uiState.fuelType.toList().toString()
            )
        }
        item { ParamRow(stringResource(R.string.param_make), uiState.make) }
        item { ParamRow(stringResource(R.string.param_model), uiState.model) }
        item { ParamRow(stringResource(R.string.param_model_year), uiState.modelYear.toString()) }

        item { Header(stringResource(R.string.header_main_driving)) }
        item { ParamRow(stringResource(R.string.param_gear_raw), uiState.gear.toString()) }
        item {
            ParamRow(
                stringResource(R.string.param_parking_brake),
                uiState.isParkingBrakeOn.toString(),
                isAlert = uiState.isParkingBrakeOn
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_odometer),
                stringResource(R.string.param_odometer_value, uiState.odometer)
            )
        }
        item { ParamRow(stringResource(R.string.param_rpm), uiState.rpm.toString()) }
        item { ParamRow(stringResource(R.string.param_range), uiState.range.toString()) }
        item {
            ParamRow(
                stringResource(R.string.param_outside_temp),
                stringResource(
                    R.string.param_outside_temp_value,
                    uiState.outsideTemp.toString(),
                    parceTempUnit(uiState.tempUnit)
                )
            )
        }

        item { Header(stringResource(R.string.header_fuel_energy)) }
        item {
            ParamRow(
                stringResource(R.string.param_fuel_level),
                stringResource(R.string.param_fuel_level_value, uiState.fuelLevel)
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_fuel_capacity),
                stringResource(R.string.param_fuel_level_value, uiState.fuelCapacity)
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_low_fuel),
                uiState.isFuelLow.toString(),
                isAlert = uiState.isFuelLow
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_ev_battery),
                "${uiState.evBatteryLevel} ${uiState.evBatteryUnits}"
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_charge_connected),
                uiState.isEvChargePortConnected.toString()
            )
        }

        item { Header(stringResource(R.string.header_engine_maintenance)) }
        item {
            ParamRow(
                stringResource(R.string.param_coolant_temp),
                stringResource(
                    R.string.param_outside_temp_value,
                    uiState.coolantTemp.toString(),
                    parceTempUnit(uiState.tempUnit)
                ),
                isAlert = uiState.coolantTemp > 105
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_oil_temp),
                stringResource(
                    R.string.param_outside_temp_value,
                    uiState.oilTemp.toString(),
                    parceTempUnit(uiState.tempUnit)
                ),
                isAlert = uiState.oilTemp > 120
            )
        }
        item { ParamRow(stringResource(R.string.param_oil_level), uiState.oilLevel.toString()) }
        item {
            ParamRow(
                stringResource(R.string.param_ignition_state),
                uiState.ignitionState.toString()
            )
        }

        item { Header(stringResource(R.string.header_safety_chassis)) }
        item {
            ParamRow(
                stringResource(R.string.param_abs_active),
                uiState.isAbsActive.toString(),
                isAlert = !uiState.isAbsActive
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_traction_active),
                uiState.isTractionControlActive.toString(),
                isAlert = !uiState.isTractionControlActive
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_door_locked),
                uiState.isDoorLocked.toString()
            )
        }

        item { Header(stringResource(R.string.header_lights)) }
        item { ParamRow(stringResource(R.string.param_turn_signal), uiState.turnSignal.toString()) }
        item {
            ParamRow(
                stringResource(R.string.param_headlights),
                uiState.headLightState.toString()
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_high_beam),
                uiState.highBeamState.toString()
            )
        }
        item { ParamRow(stringResource(R.string.param_night_mode), uiState.nighMode.toString()) }

        item { Header(stringResource(R.string.header_ev_charge)) }
        item {
            ParamRow(
                stringResource(R.string.param_ev_charge_state),
                uiState.evCharge.toString()
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_ev_charge_time),
                uiState.evChargeTime.toString()
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_ev_regeneration),
                uiState.isEvRecuperate.toString()
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_ev_battery_capacity),
                uiState.evBatteryCapacity.toString()
            )
        }

        // Dynamic Tire Pressure Rows
        uiState.tirePressures.forEach { (area, pressure) ->
            item {
                ParamRow(
                    stringResource(R.string.param_tire_pressure_format, area),
                    stringResource(R.string.param_tire_pressure_unit, pressure),
                    isAlert = uiState.isCriticallyLowTirePressure
                )
            }
        }

        item { Header(stringResource(R.string.header_diagnostic_obd)) }
        item {
            ParamRow(
                stringResource(R.string.param_check_engine),
                uiState.isCheckEngineOn.toString(),
                isAlert = uiState.isCheckEngineOn
            )
        }
        item {
            ParamRow(
                stringResource(R.string.param_obd_data),
                uiState.obdData.ifBlank { stringResource(R.string.param_no_data) })
        }
        item {
            ParamRow(
                stringResource(R.string.param_active_dtcs),
                uiState.activeDTCs.joinToString().ifBlank { stringResource(R.string.param_none) })
        }
    }
}

@Composable
private fun parceTempUnit(unit: String): String =
    if (unit == stringResource(R.string.fahrenheit)) stringResource(R.string.fahrenheit_degree) else stringResource(
        R.string.celsius_degree
    )


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
fun ParamRow(label: String, value: String = "", isAlert: Boolean = false) {
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