package app.hypostats.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.hypostats.data.local.AppDataStore
import app.hypostats.domain.BackupService
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val backupService: BackupService,
        private val appDataStore: AppDataStore,
    ) : ViewModel() {
        private val selectedLanguage = MutableStateFlow(getCurrentLanguage())

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

        private fun getCurrentLanguage(): AppLanguage {
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            return when {
                currentLocales.isEmpty -> AppLanguage.SYSTEM
                currentLocales[0]?.language == "cs" -> AppLanguage.CZECH
                currentLocales[0]?.language == "en" -> AppLanguage.ENGLISH
                else -> AppLanguage.SYSTEM
            }
        }

        fun selectLanguage(language: AppLanguage) {
            selectedLanguage.value = language

            val localeList =
                when (language) {
                    AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
                    AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
                    AppLanguage.CZECH -> LocaleListCompat.forLanguageTags("cs")
                }

            AppCompatDelegate.setApplicationLocales(localeList)
        }

        fun selectTheme(theme: AppTheme) {
            viewModelScope.launch {
                appDataStore.setAppTheme(theme)
            }
        }

        suspend fun exportBackup(directory: File): Result<File> {
            val backupFile = File(directory, "backup.json")
            return backupService.exportToFile(backupFile)
        }

        suspend fun importBackup(directory: File): Result<Unit> {
            val backupFile = File(directory, "backup.json")
            return backupService.importFromFile(backupFile)
        }
    }
