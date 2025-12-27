package app.hypostats.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R
import app.hypostats.ui.model.SettingsUiState
import app.hypostats.ui.settings.sections.BackupSection
import app.hypostats.ui.settings.sections.CarbIncrementSection
import app.hypostats.ui.settings.sections.LanguageSection
import app.hypostats.ui.settings.sections.ThemeSection
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val exportLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { nullableUri ->
            nullableUri?.let { uri ->
                scope.launch {
                    viewModel
                        .exportBackup(uri)
                        .onSuccess {
                            snackbarHostState.showSnackbar(message = context.getString(R.string.export_success))
                        }.onFailure { error ->
                            if (error is IOException) {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.export_error, error.message),
                                )
                            }
                        }
                }
            }
        }

    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { nullableUri ->
            nullableUri?.let { uri ->
                scope.launch {
                    viewModel
                        .importBackup(uri)
                        .onSuccess {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.import_success),
                            )
                        }.onFailure { error ->
                            when (error) {
                                is IOException -> {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.import_io_error, error.message),
                                    )
                                }
                            }
                        }
                }
            }
        }

    SettingsLayout {
        SettingsScreenContent(
            state = state,
            onEvent = { event ->
                when (event) {
                    is SettingsEvent.SelectLanguage -> viewModel.selectLanguage(event.language)
                    is SettingsEvent.SelectTheme -> viewModel.selectTheme(event.theme)
                    is SettingsEvent.SetCarbIncrement -> viewModel.setCarbIncrement(event.increment)
                    SettingsEvent.ExportBackup -> exportLauncher.launch("backup.json")
                    SettingsEvent.ImportBackup -> importLauncher.launch("application/json")
                }
            },
        )
    }
}

@Composable
private fun SettingsLayout(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
private fun SettingsScreenContent(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
) {
    LanguageSection(
        selectedLanguage = state.selectedLanguage,
        onLanguageSelected = { onEvent(SettingsEvent.SelectLanguage(it)) },
    )

    ThemeSection(
        selectedTheme = state.selectedTheme,
        onThemeSelected = { onEvent(SettingsEvent.SelectTheme(it)) },
    )

    CarbIncrementSection(
        carbIncrement = state.carbIncrement,
        onCarbIncrementSelected = { onEvent(SettingsEvent.SetCarbIncrement(it)) },
    )

    BackupSection(
        onExportClick = { onEvent(SettingsEvent.ExportBackup) },
        onImportClick = { onEvent(SettingsEvent.ImportBackup) },
    )
}

@Preview(name = "Portrait")
@Preview(name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun SettingsScreenContentPreview() {
    MaterialTheme {
        SettingsLayout {
            SettingsScreenContent(
                state = SettingsUiState(),
                onEvent = {},
            )
        }
    }
}
