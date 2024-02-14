package com.kgurgul.cpuinfo.features.information.sensors

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kgurgul.cpuinfo.domain.model.SensorData
import com.kgurgul.cpuinfo.ui.components.CpuDivider
import com.kgurgul.cpuinfo.ui.theme.CpuInfoTheme
import com.kgurgul.cpuinfo.ui.theme.spacingSmall
import com.kgurgul.cpuinfo.ui.theme.spacingXSmall

@Composable
fun SensorsInfoScreen(
    viewModel: SensorsInfoViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.startProvidingData()
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.stopProvidingData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    SensorsInfoScreen(
        uiState = uiState,
    )
}

@Composable
fun SensorsInfoScreen(
    uiState: SensorsInfoViewModel.UiState,
) {
    LazyColumn(
        contentPadding = PaddingValues(spacingSmall),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(
            uiState.sensors,
            key = { _, sensorData -> sensorData.id }
        ) { index, sensorData ->
            SensorItem(
                title = sensorData.name,
                value = sensorData.value,
                isLastItem = index == uiState.sensors.lastIndex
            )
        }
    }
}

@Composable
private fun SensorItem(
    title: String,
    value: String,
    isLastItem: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .focusable(),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.size(spacingXSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f),
        )
    }
    if (!isLastItem) {
        CpuDivider(
            modifier = Modifier.padding(top = spacingSmall),
        )
    }
}

@Preview
@Composable
fun SensorsInfoScreenPreview() {
    CpuInfoTheme {
        SensorsInfoScreen(
            uiState = SensorsInfoViewModel.UiState(
                listOf(
                    SensorData(
                        id = "test",
                        name = "test",
                        value = "",
                    ),
                    SensorData(
                        id = "test",
                        name = "test",
                        value = "test",
                    ),
                )
            ),
        )
    }
}
