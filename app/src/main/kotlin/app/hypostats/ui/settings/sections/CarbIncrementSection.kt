package app.hypostats.ui.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.hypostats.R
import app.hypostats.ui.settings.components.ExpandableSettingSection

@Composable
fun CarbIncrementSection(
    carbIncrement: Int,
    onCarbIncrementSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExpandableSettingSection(
        title = stringResource(R.string.carb_increment),
        currentValue = stringResource(R.string.current_carb_increment, carbIncrement),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.current_carb_increment, carbIncrement),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f, fill = false),
            )
            Slider(
                value = carbIncrement.toFloat(),
                onValueChange = { onCarbIncrementSelected(it.toInt()) },
                valueRange = 1f..15f,
                steps = 13,
                modifier = Modifier.weight(weight = 4f),
            )
        }
    }
}

@Preview
@Composable
private fun CarbIncrementSectionPreview() {
    MaterialTheme {
        CarbIncrementSection(
            carbIncrement = 5,
            onCarbIncrementSelected = { },
        )
    }
}
