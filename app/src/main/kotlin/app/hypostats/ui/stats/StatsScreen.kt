package app.hypostats.ui.stats

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R

@Composable
fun StatsScreen(viewModel: StatsViewModel = hiltViewModel()) {

    val stats by viewModel.stats.collectAsStateWithLifecycle()

    val statsText = if (stats.treatments.isEmpty()) {
        stringResource(R.string.no_treatments_recorded_yet)
    } else {
        """
     ${stringResource(R.string.total_episodes, stats.totalEpisodes)}
     ${stringResource(R.string.period_days, stats.daySpan)}
     """.trimIndent()
    }

    StatsLayout {
        BasicStatisticsCard(statsText)
    }
}

@Composable
private fun StatsLayout(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
private fun BasicStatisticsCard(stats: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stats)
        }
    }
}

// Preview functions
@Preview(showBackground = true)
@Composable
private fun BasicStatisticsCardPreview() {
    MaterialTheme {
        BasicStatisticsCard(
            stats = """
                Total Episodes: 5
                Period: 42 days
            """.trimIndent()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicStatisticsCardEmptyPreview() {
    MaterialTheme {
        BasicStatisticsCard(stats = "No treatments recorded yet.")
    }
}
