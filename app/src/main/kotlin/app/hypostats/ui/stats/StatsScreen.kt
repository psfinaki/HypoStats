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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hypostats.R
import app.hypostats.domain.model.GeneralStats
import app.hypostats.domain.model.HypoHour
import app.hypostats.domain.model.Stats

@Composable
fun StatsScreen(viewModel: StatsViewModel = hiltViewModel()) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    StatsScreenContent(stats)
}

@Composable
private fun StatsScreenContent(stats: Stats) {
    StatsLayout {
        GeneralStatsCard(stats.generalStats)
        if (stats.topHypoHours.isNotEmpty()) {
            TopHypoHoursCard(stats.topHypoHours)
        }
    }
}

@Composable
private fun StatsLayout(content: @Composable ColumnScope.() -> Unit) {
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
private fun GeneralStatsCard(stats: GeneralStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.general_stats_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(text = stringResource(R.string.total_episodes, stats.totalEpisodes))
            Text(text = stringResource(R.string.period_days, stats.daySpan))
            Text(text = stringResource(R.string.current_streak, stats.currentStreak))
            Text(text = stringResource(R.string.longest_streak, stats.longestStreak))
        }
    }
}

@Composable
private fun TopHypoHoursCard(topHypoHours: List<HypoHour>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.top_hypo_hours_title),
                style = MaterialTheme.typography.titleMedium,
            )
            topHypoHours.forEach { hypoHour ->
                Text(
                    text =
                        stringResource(
                            R.string.hypo_hour,
                            hypoHour.hour,
                            hypoHour.hour + 1,
                            hypoHour.numberOfHypos,
                        ),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Portrait")
@Preview(showBackground = true, name = "Landscape", widthDp = 800, heightDp = 300)
@Composable
private fun StatsScreenPreview() {
    MaterialTheme {
        StatsScreenContent(
            stats =
                Stats(
                    generalStats =
                        GeneralStats(
                            totalEpisodes = 5,
                            daySpan = 42,
                            currentStreak = 2,
                            longestStreak = 5,
                        ),
                    topHypoHours = listOf(HypoHour(2, 10), HypoHour(5, 9)),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralStatsCardPreview() {
    MaterialTheme {
        GeneralStatsCard(
            stats =
                GeneralStats(
                    totalEpisodes = 5,
                    daySpan = 42,
                    currentStreak = 2,
                    longestStreak = 5,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GeneralStatsCardEmptyPreview() {
    MaterialTheme {
        GeneralStatsCard(stats = GeneralStats.Empty)
    }
}

@Preview(showBackground = true)
@Composable
private fun TopHypoHoursCardPreview() {
    MaterialTheme {
        TopHypoHoursCard(topHypoHours = listOf(HypoHour(2, 10), HypoHour(5, 9), HypoHour(7, 8)))
    }
}
