package app.hypostats.ui.settings

import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.model.AppTheme

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

    data object ExportBackup : SettingsEvent

    data object ImportBackup : SettingsEvent
}
