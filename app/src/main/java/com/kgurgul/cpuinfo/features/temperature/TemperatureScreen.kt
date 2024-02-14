package com.kgurgul.cpuinfo.features.temperature

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kgurgul.cpuinfo.R
import com.kgurgul.cpuinfo.data.local.StubUserPreferencesRepository
import com.kgurgul.cpuinfo.domain.model.TemperatureItem
import com.kgurgul.cpuinfo.ui.components.PrimaryTopAppBar
import com.kgurgul.cpuinfo.ui.theme.CpuInfoTheme
import com.kgurgul.cpuinfo.ui.theme.spacingMedium
import com.kgurgul.cpuinfo.ui.theme.spacingSmall
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    TemperatureScreen(
        uiState = uiState,
    )
}

@Composable
fun TemperatureScreen(
    uiState: TemperatureViewModel.UiState,
) {
    Scaffold(
        topBar = {
            PrimaryTopAppBar(
                title = stringResource(id = R.string.temperature),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        val paddingModifier = Modifier.padding(paddingValues)
        if (!uiState.isLoading && uiState.temperatureItems.isEmpty()) {
            EmptyTemperatureList(
                modifier = paddingModifier,
            )
        } else {
            TemperatureList(
                temperatureItems = uiState.temperatureItems,
                temperatureFormatter = uiState.temperatureFormatter,
                modifier = paddingModifier,
            )
        }
    }
}

@Composable
private fun TemperatureList(
    temperatureItems: ImmutableList<TemperatureItem>,
    temperatureFormatter: TemperatureFormatter,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        items(
            items = temperatureItems,
            key = { item -> item.id },
        ) { item ->
            TemperatureItem(
                item = item,
                temperatureFormatter = temperatureFormatter,
            )
        }
    }
}

@Composable
private fun TemperatureItem(
    item: TemperatureItem,
    temperatureFormatter: TemperatureFormatter
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(spacingMedium)
            .focusable(),
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = null,
            modifier = Modifier.requiredSize(60.dp),
        )
        Spacer(modifier = Modifier.requiredSize(spacingSmall))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.requiredSize(spacingSmall))
            Text(
                text = temperatureFormatter.format(item.temperature),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun EmptyTemperatureList(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium)
            .then(modifier),
    ) {
        Text(
            text = stringResource(id = R.string.no_temp_data),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun TemperatureScreenPreview() {
    CpuInfoTheme {
        TemperatureScreen(
            uiState = TemperatureViewModel.UiState(
                temperatureFormatter = TemperatureFormatter(StubUserPreferencesRepository()),
                isLoading = false,
                temperatureItems = persistentListOf(
                    TemperatureItem(
                        id = 0,
                        iconRes = R.drawable.ic_cpu_temp,
                        name = "CPU",
                        temperature = 30f
                    ),
                    TemperatureItem(
                        id = 1,
                        iconRes = R.drawable.ic_battery,
                        name = "Battery",
                        temperature = 30f
                    )
                )
            ),
        )
    }
}