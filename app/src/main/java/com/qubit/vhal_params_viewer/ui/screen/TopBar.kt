package com.qubit.vhal_params_viewer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qubit.vhal_params_viewer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.diagnostics_toggle), color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxHeight()
                            .clickable(onClick = onClick),
                    ) {
                        Text(
                            text = if (checked) stringResource(R.string.mock) else stringResource(
                                R.string.real
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                            color = if (checked) Color.Green else Color.Red
                        )
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = checked,
                            onCheckedChange = onCheckedChange,
                            thumbContent = {
                                Icon(
                                    imageVector = if (checked) Icons.Default.Build else Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        content.invoke(padding)//VhalDataContent(modifier = Modifier.padding(padding), uiState = uiState)
    }
}