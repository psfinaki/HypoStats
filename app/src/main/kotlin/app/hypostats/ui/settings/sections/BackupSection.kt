package app.hypostats.ui.settings.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.hypostats.R

@Composable
fun BackupSection(
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.backup),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp),
            )
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

@Preview
@Composable
private fun BackupSectionPreview() {
    MaterialTheme {
        BackupSection(
            onExportClick = { },
            onImportClick = { },
        )
    }
}
