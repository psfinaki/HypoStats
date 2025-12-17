package app.hypostats.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.domain.BackupService
import app.hypostats.domain.LanguageManager
import app.hypostats.domain.SettingsRepository
import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.model.AppTheme
import app.hypostats.ui.model.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val backupService: BackupService,
        private val settingsRepository: SettingsRepository,
        private val languageManager: LanguageManager,
    ) : ViewModel() {
        private val selectedLanguage = MutableStateFlow(languageManager.getCurrentLanguage())

        val state: StateFlow<SettingsUiState> =
            combine(
                settingsRepository.getAppTheme(),
                selectedLanguage,
                settingsRepository.getCarbIncrement(),
            ) { theme, language, carbIncrement ->
                SettingsUiState(
                    selectedLanguage = language,
                    selectedTheme = theme,
                    carbIncrement = carbIncrement,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SettingsUiState(),
            )

        fun selectLanguage(language: AppLanguage) {
            selectedLanguage.value = language
            languageManager.setLanguage(language)
        }

        fun selectTheme(theme: AppTheme) {
            viewModelScope.launch {
                settingsRepository.setAppTheme(theme)
            }
        }

        fun setCarbIncrement(increment: Int) {
            viewModelScope.launch {
                settingsRepository.setCarbIncrement(increment)
            }
        }

        suspend fun exportBackup(uri: Uri): Result<Unit> = backupService.exportToFile(uri)

        suspend fun importBackup(uri: Uri): Result<Unit> = backupService.importFromFile(uri)
    }
