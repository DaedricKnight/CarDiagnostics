package com.qubit.vhal_params_viewer.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qubit.core_vhal.VhalParamsState
import com.qubit.vhal_params_viewer.R
import com.qubit.vhal_params_viewer.viewmodel.CarDiagnosticsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: CarDiagnosticsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toastString = stringResource(R.string.toast_no_real_mode)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    TopBar(
        checked = true,
        snackbarHostState = snackbarHostState,
        onClick = {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = toastString,
                    duration = SnackbarDuration.Short
                )
            }
        },
        content = { padding ->
            DashboardContent(modifier = Modifier.padding(padding), uiState = uiState)
        })
}

@Composable
fun SpeedometerGauge(speed: Float) {
    val animatedSpeed by animateFloatAsState(targetValue = speed, label = "speed")

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.DarkGray,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF00E5FF),
                startAngle = 150f,
                sweepAngle = (animatedSpeed / 250f) * 240f, // Until 250 km/h
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = animatedSpeed.toInt().toString(),
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 100.sp),
                color = Color.White
            )
            Text(
                text = stringResource(R.string.km_h),
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 50.sp),
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun VerticalGauge(
    progress: Float,
    icon: ImageVector,
    isWarning: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .width(24.dp)
                .weight(1f)
                .background(Color.DarkGray.copy(alpha = 0.5f), shape = RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(progress.coerceIn(0f, 1f))
                    .background(
                        if (isWarning) Color.Red else Color.Cyan,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

@Composable
fun DashboardContent(modifier: Modifier, uiState: VhalParamsState) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (uiState.isParkingBrakeOn) Color(0xFF440000) else Color.Black)
            .padding(24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            VerticalGauge(
                progress = uiState.fuelLevel / uiState.fuelCapacity,
                icon = Icons.Default.LocalGasStation,
                isWarning = uiState.isFuelLow,
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpeedometerGauge(speed = uiState.speed)

            Spacer(modifier = Modifier.height(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.param_gear_raw, uiState.gear),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Cyan
                )
                Spacer(modifier = Modifier.width(30.dp))

                Text(
                    text = stringResource(R.string.param_parking_brake),
                    style = MaterialTheme.typography.displayMedium,
                    color = if (uiState.isParkingBrakeOn) Color.Red else Color.DarkGray
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalGauge(
                progress = uiState.oilLevel / 100f,
                icon = Icons.Default.OilBarrel,
                isWarning = false,
            )
        }
    }
}