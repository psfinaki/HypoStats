package app.hypostats.ui.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R
import app.hypostats.domain.model.Treatment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun LogScreen(viewModel: LogViewModel = hiltViewModel()) {
    val treatments by viewModel.treatments.collectAsStateWithLifecycle()

    LogLayout {
        TreatmentLogCard(treatments)
    }
}

@Composable
private fun LogLayout(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content,
    )
}

@Composable
private fun TreatmentLogCard(treatments: List<Treatment>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (treatments.isEmpty()) {
            Text(
                text = stringResource(R.string.no_treatments_recorded_yet),
                modifier = Modifier.padding(16.dp),
            )
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                treatments.forEach { treatment ->
                    TreatmentItem(treatment)
                }
            }
        }
    }
}

@Composable
private fun TreatmentItem(treatment: Treatment) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss") }
    val dateTime =
        remember(treatment.timestamp) {
            treatment.timestamp.atZone(ZoneId.systemDefault()).format(formatter)
        }

    Text(
        text = stringResource(R.string.treatment_entry, dateTime, treatment.sugarAmount),
    )
}

@Preview(showBackground = true, name = "Portrait")
@Preview(showBackground = true, name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun LogScreenPreview() {
    MaterialTheme {
        LogLayout {
            TreatmentLogCard(treatments = emptyList())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TreatmentLogCardPreview() {
    MaterialTheme {
        TreatmentLogCard(
            treatments =
                listOf(
                    Treatment(
                        timestamp = Instant.ofEpochMilli(1727280615000), // 25/09/25 14:30:15
                        sugarAmount = 15,
                    ),
                    Treatment(
                        timestamp = Instant.ofEpochMilli(1727194522000), // 24/09/25 09:45:22
                        sugarAmount = 10,
                    ),
                    Treatment(
                        timestamp = Instant.ofEpochMilli(1727110810000), // 23/09/25 16:20:10
                        sugarAmount = 20,
                    ),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TreatmentLogCardEmptyPreview() {
    MaterialTheme {
        TreatmentLogCard(treatments = emptyList())
    }
}

@Preview(showBackground = true)
@Composable
private fun TreatmentItemPreview() {
    MaterialTheme {
        TreatmentItem(
            treatment =
                Treatment(
                    timestamp = Instant.ofEpochMilli(1727280615000), // 25/09/25 14:30:15
                    sugarAmount = 15,
                ),
        )
    }
}
