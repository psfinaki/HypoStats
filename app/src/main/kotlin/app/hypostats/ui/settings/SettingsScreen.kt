package app.hypostats.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R
import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.SettingsUiState
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
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column {
            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LanguageOptions(state.selectedLanguage, onLanguageSelected)
            }
        }

        Column {
            Text(
                text = stringResource(R.string.theme),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                ThemeOptions(state.selectedTheme, onThemeSelected)
            }
        }

        Column {
            Text(
                text = stringResource(R.string.backup),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = onExportClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.export_backup))
                    }

                    Button(
                        onClick = onImportClick,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        Text(stringResource(R.string.import_backup))
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageOptions(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    Column {
        LanguageOption(
            label = stringResource(R.string.language_system),
            selected = selectedLanguage == AppLanguage.SYSTEM,
            onClick = { onLanguageSelected(AppLanguage.SYSTEM) },
        )
        LanguageOption(
            label = stringResource(R.string.language_english),
            selected = selectedLanguage == AppLanguage.ENGLISH,
            onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
        )
        LanguageOption(
            label = stringResource(R.string.language_czech),
            selected = selectedLanguage == AppLanguage.CZECH,
            onClick = { onLanguageSelected(AppLanguage.CZECH) },
        )
    }
}

@Composable
private fun ThemeOptions(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    Column {
        ThemeOption(
            label = stringResource(R.string.theme_system),
            selected = selectedTheme == AppTheme.SYSTEM,
            onClick = { onThemeSelected(AppTheme.SYSTEM) },
        )
        ThemeOption(
            label = stringResource(R.string.theme_light),
            selected = selectedTheme == AppTheme.LIGHT,
            onClick = { onThemeSelected(AppTheme.LIGHT) },
        )
        ThemeOption(
            label = stringResource(R.string.theme_dark),
            selected = selectedTheme == AppTheme.DARK,
            onClick = { onThemeSelected(AppTheme.DARK) },
        )
    }
}

@Composable
private fun LanguageOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Preview(showBackground = true, name = "Portrait")
@Preview(showBackground = true, name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun SettingsScreenContentPreview() {
    MaterialTheme {
        SettingsScreenContent(
            state = SettingsUiState(),
            onLanguageSelected = { },
            onThemeSelected = { },
            onExportClick = { },
            onImportClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageOptionSelectedPreview() {
    MaterialTheme {
        LanguageOption(
            label = "English",
            selected = true,
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageOptionUnselectedPreview() {
    MaterialTheme {
        LanguageOption(
            label = "Čeština",
            selected = false,
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeOptionSelectedPreview() {
    MaterialTheme {
        ThemeOption(
            label = "Dark",
            selected = true,
            onClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeOptionUnselectedPreview() {
    MaterialTheme {
        ThemeOption(
            label = "Light",
            selected = false,
            onClick = { },
        )
    }
}
