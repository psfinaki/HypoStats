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
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.settings.components.ExpandableSettingSection
import app.hypostats.ui.settings.components.SettingOption

@Composable
fun ThemeSection(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExpandableSettingSection(
        title = stringResource(R.string.theme),
        currentValue = getThemeLabel(selectedTheme),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Column {
            SettingOption(
                label = stringResource(R.string.theme_system),
                selected = selectedTheme == AppTheme.SYSTEM,
                onClick = { onThemeSelected(AppTheme.SYSTEM) },
            )
            SettingOption(
                label = stringResource(R.string.theme_light),
                selected = selectedTheme == AppTheme.LIGHT,
                onClick = { onThemeSelected(AppTheme.LIGHT) },
            )
            SettingOption(
                label = stringResource(R.string.theme_dark),
                selected = selectedTheme == AppTheme.DARK,
                onClick = { onThemeSelected(AppTheme.DARK) },
            )
        }
    }
}

@Composable
private fun getThemeLabel(theme: AppTheme): String =
    when (theme) {
        AppTheme.SYSTEM -> stringResource(R.string.theme_system)
        AppTheme.LIGHT -> stringResource(R.string.theme_light)
        AppTheme.DARK -> stringResource(R.string.theme_dark)
    }

@Preview
@Composable
private fun ThemeSectionPreview() {
    ThemeSection(
        selectedTheme = AppTheme.SYSTEM,
        onThemeSelected = { },
    )
}
