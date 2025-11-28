package app.hypostats.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.local.AppDataStore
import app.hypostats.domain.BackupService
import app.hypostats.domain.LanguageManager
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
        private val appDataStore: AppDataStore,
        private val languageManager: LanguageManager,
    ) : ViewModel() {
        private val selectedLanguage = MutableStateFlow(languageManager.getCurrentLanguage())

        val state: StateFlow<SettingsUiState> =
            combine(
                appDataStore.appTheme,
                selectedLanguage,
            ) { theme, language ->
                SettingsUiState(
                    selectedLanguage = language,
                    selectedTheme = theme ?: AppTheme.SYSTEM,
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
                appDataStore.setAppTheme(theme)
            }
        }

        suspend fun exportBackup(uri: Uri): Result<Unit> = backupService.exportToFile(uri)

        suspend fun importBackup(uri: Uri): Result<Unit> = backupService.importFromFile(uri)
    }
