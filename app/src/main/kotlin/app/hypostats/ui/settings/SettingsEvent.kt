package app.hypostats.ui.settings

import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.CarbIcon

sealed interface SettingsEvent {
    data class SelectLanguage(
        val language: AppLanguage,
    ) : SettingsEvent

    data class SelectTheme(
        val theme: AppTheme,
    ) : SettingsEvent

    data class SetCarbIncrement(
        val increment: Int,
    ) : SettingsEvent

    data class SelectCarbIcon(
        val icon: CarbIcon,
    ) : SettingsEvent

    data object ExportBackup : SettingsEvent

    data object ImportBackup : SettingsEvent
}
