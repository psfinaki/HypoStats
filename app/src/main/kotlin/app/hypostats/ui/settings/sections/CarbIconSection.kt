package app.hypostats.ui.settings.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.hypostats.R
import app.hypostats.ui.model.CarbIcon
import app.hypostats.ui.settings.components.ExpandableSettingSection
import app.hypostats.ui.settings.components.SettingOption

@Composable
fun CarbIconSection(
    selectedIcon: CarbIcon,
    onIconSelected: (CarbIcon) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExpandableSettingSection(
        title = stringResource(R.string.carb_icon),
        currentValue = getIconLabel(selectedIcon),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Column {
            SettingOption(
                label = stringResource(R.string.carb_icon_sugar),
                selected = selectedIcon == CarbIcon.SUGAR,
                onClick = { onIconSelected(CarbIcon.SUGAR) },
            )
            SettingOption(
                label = stringResource(R.string.carb_icon_cola),
                selected = selectedIcon == CarbIcon.COLA,
                onClick = { onIconSelected(CarbIcon.COLA) },
            )
            SettingOption(
                label = stringResource(R.string.carb_icon_candies),
                selected = selectedIcon == CarbIcon.CANDIES,
                onClick = { onIconSelected(CarbIcon.CANDIES) },
            )
        }
    }
}

@Composable
private fun getIconLabel(icon: CarbIcon): String =
    when (icon) {
        CarbIcon.SUGAR -> stringResource(R.string.carb_icon_sugar)
        CarbIcon.COLA -> stringResource(R.string.carb_icon_cola)
        CarbIcon.CANDIES -> stringResource(R.string.carb_icon_candies)
    }

@Preview
@Composable
private fun CarbIconSectionPreview() {
    CarbIconSection(
        selectedIcon = CarbIcon.SUGAR,
        onIconSelected = { },
    )
}
