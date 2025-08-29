package app.hypostats.ui.model

enum class AppLanguage {
    SYSTEM,
    ENGLISH,
    CZECH
}

data class SettingsUiState(
    val selectedLanguage: AppLanguage = AppLanguage.SYSTEM
)
