package app.hypostats.ui.settings

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
import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.SettingsUiState
import app.hypostats.ui.settings.sections.BackupSection
import app.hypostats.ui.settings.sections.LanguageSection
import app.hypostats.ui.settings.sections.ThemeSection
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    SettingsLayout {
        SettingsScreenContent(
            state = state,
            onLanguageSelected = viewModel::selectLanguage,
            onThemeSelected = viewModel::selectTheme,
            onExportClick = {
                scope.launch {
                    val result = viewModel.exportBackup(context.filesDir)
                    result
                        .onSuccess { backupFile ->
                            snackbarHostState.showSnackbar(
                                message =
                                    context.getString(
                                        R.string.export_success,
                                        backupFile.absolutePath,
                                    ),
                            )
                        }.onFailure { error ->
                            if (error is IOException) {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.export_error, error.message),
                                )
                            }
                        }
                }
            },
            onImportClick = {
                scope.launch {
                    val result = viewModel.importBackup(context.filesDir)
                    result
                        .onSuccess {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.import_success),
                            )
                        }.onFailure { error ->
                            when (error) {
                                is FileNotFoundException -> {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.import_file_not_found),
                                    )
                                }
                                is IOException -> {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.import_io_error, error.message),
                                    )
                                }
                            }
                        }
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
    onLanguageSelected: (AppLanguage) -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
) {
    LanguageSection(
        selectedLanguage = state.selectedLanguage,
        onLanguageSelected = onLanguageSelected,
    )

    ThemeSection(
        selectedTheme = state.selectedTheme,
        onThemeSelected = onThemeSelected,
    )

    BackupSection(
        onExportClick = onExportClick,
        onImportClick = onImportClick,
    )
}

@Preview(showBackground = true, name = "Portrait")
@Preview(showBackground = true, name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun SettingsScreenContentPreview() {
    MaterialTheme {
        SettingsLayout {
            SettingsScreenContent(
                state = SettingsUiState(),
                onLanguageSelected = { },
                onThemeSelected = { },
                onExportClick = { },
                onImportClick = { },
            )
        }
    }
}
