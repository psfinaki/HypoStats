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
import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.settings.components.ExpandableSettingSection
import app.hypostats.ui.settings.components.SettingOption

@Composable
fun LanguageSection(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExpandableSettingSection(
        title = stringResource(R.string.language),
        currentValue = getLanguageLabel(selectedLanguage),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Column {
            SettingOption(
                label = stringResource(R.string.language_system),
                selected = selectedLanguage == AppLanguage.SYSTEM,
                onClick = { onLanguageSelected(AppLanguage.SYSTEM) },
            )
            SettingOption(
                label = stringResource(R.string.language_english),
                selected = selectedLanguage == AppLanguage.ENGLISH,
                onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
            )
            SettingOption(
                label = stringResource(R.string.language_czech),
                selected = selectedLanguage == AppLanguage.CZECH,
                onClick = { onLanguageSelected(AppLanguage.CZECH) },
            )
        }
    }
}

@Composable
private fun getLanguageLabel(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM -> stringResource(R.string.language_system)
        AppLanguage.ENGLISH -> stringResource(R.string.language_english)
        AppLanguage.CZECH -> stringResource(R.string.language_czech)
    }

@Preview(showBackground = true)
@Composable
private fun LanguageSectionPreview() {
    LanguageSection(
        selectedLanguage = AppLanguage.ENGLISH,
        onLanguageSelected = { },
    )
}
