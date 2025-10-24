package app.hypostats.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    SettingsLayout {
        SettingsScreenContent(
            selectedLanguage = state.selectedLanguage,
            onLanguageSelected = viewModel::selectLanguage,
            onExportClick = { viewModel.exportBackup(context.filesDir) },
            onImportClick = { viewModel.importBackup(context.filesDir) }
        )
    }
}

@Composable
private fun SettingsLayout(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
private fun SettingsScreenContent(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column {
            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    LanguageOption(
                        label = stringResource(R.string.language_system),
                        selected = selectedLanguage == AppLanguage.SYSTEM,
                        onClick = { onLanguageSelected(AppLanguage.SYSTEM) }
                    )
                    
                    LanguageOption(
                        label = stringResource(R.string.language_english),
                        selected = selectedLanguage == AppLanguage.ENGLISH,
                        onClick = { onLanguageSelected(AppLanguage.ENGLISH) }
                    )
                    
                    LanguageOption(
                        label = stringResource(R.string.language_czech),
                        selected = selectedLanguage == AppLanguage.CZECH,
                        onClick = { onLanguageSelected(AppLanguage.CZECH) }
                    )
                }
            }
        }
        
        Column {
            Text(
                text = stringResource(R.string.backup),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = onExportClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.export_backup))
                    }
                    
                    Button(
                        onClick = onImportClick,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text(stringResource(R.string.import_backup))
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPreview() {
    MaterialTheme {
        SettingsScreenContent(
            selectedLanguage = AppLanguage.ENGLISH,
            onLanguageSelected = { },
            onExportClick = { },
            onImportClick = { }
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
            onClick = { }
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
            onClick = { }
        )
    }
}
