package app.hypostats.ui.hypo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R
import app.hypostats.ui.model.HypoUiState
import kotlinx.coroutines.launch

@Composable
fun HypoScreen(
    viewModel: HypoViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    HypoScreenContent(
        uiState = state,
        onAddSugar = viewModel::addSugar,
        onAddOffset = viewModel::addOffset,
        onReset = viewModel::resetTreatment,
        onSubmit = {
            viewModel.saveTreatment {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.treatment_saved),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        },
    )
}

@Composable
private fun SugarAmountDisplay(amount: Int) {
    Text(
        text = stringResource(R.string.g_sugar, amount),
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 32.dp),
    )
}

@Composable
private fun AddSugarButton(onAddSugar: () -> Unit) {
    FloatingActionButton(
        onClick = onAddSugar,
        modifier = Modifier.size(210.dp).padding(bottom = 32.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.sugar),
            contentDescription = stringResource(R.string.eat_5g_sugar),
            modifier = Modifier.size(140.dp),
        )
    }
}

@Composable
private fun OffsetControls(
    currentOffset: Int,
    onAddOffset: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
    ) {
        Text(
            text = stringResource(R.string.offset),
            fontSize = 18.sp,
        )

        OutlinedIconButton(
            onClick = onAddOffset,
            modifier = Modifier.size(40.dp),
        ) {
            Text(
                text = "−",
                fontSize = 24.sp,
            )
        }

        Text(
            text = stringResource(R.string.offset_minutes, currentOffset),
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun ActionButtons(
    currentAmount: Int,
    onReset: () -> Unit,
    onSubmit: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Button(onClick = onReset) {
            Text(stringResource(R.string.reset))
        }

        Button(
            onClick = onSubmit,
            enabled = currentAmount > 0,
        ) {
            Text(stringResource(R.string.submit_treatment))
        }
    }
}

@Composable
private fun HypoScreenContent(
    uiState: HypoUiState,
    onAddSugar: () -> Unit,
    onAddOffset: () -> Unit,
    onReset: () -> Unit,
    onSubmit: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SugarAmountDisplay(uiState.sugarAmount)
        AddSugarButton(onAddSugar = onAddSugar)
        OffsetControls(
            currentOffset = uiState.offsetMinutes,
            onAddOffset = onAddOffset,
        )
        ActionButtons(
            currentAmount = uiState.sugarAmount,
            onReset = onReset,
            onSubmit = onSubmit,
        )
    }
}

@Preview(showBackground = true, name = "Portrait")
@Preview(showBackground = true, name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun HypoScreenPreview() {
    MaterialTheme {
        HypoScreenContent(
            HypoUiState.Empty,
            onAddSugar = { },
            onAddOffset = { },
            onReset = { },
            onSubmit = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SugarAmountDisplayPreview() {
    MaterialTheme {
        SugarAmountDisplay(amount = 15)
    }
}

@Preview(showBackground = true)
@Composable
private fun OffsetControlsPreview() {
    MaterialTheme {
        OffsetControls(
            currentOffset = 30,
            onAddOffset = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonsEnabledPreview() {
    MaterialTheme {
        ActionButtons(
            currentAmount = 15,
            onReset = { },
            onSubmit = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonsDisabledPreview() {
    MaterialTheme {
        ActionButtons(
            currentAmount = 0,
            onReset = { },
            onSubmit = { },
        )
    }
}
