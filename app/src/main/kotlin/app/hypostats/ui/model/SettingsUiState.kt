package app.hypostats.ui.model

data class SettingsUiState(
    val selectedLanguage: AppLanguage = AppLanguage.SYSTEM,
    val selectedTheme: AppTheme = AppTheme.SYSTEM,
    val carbIncrement: Int = 5,
)
