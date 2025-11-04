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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        private val _state = MutableStateFlow(SettingsUiState())
        val state: StateFlow<SettingsUiState> = _state.asStateFlow()

        init {
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            val currentLanguage =
                when {
                    currentLocales.isEmpty -> AppLanguage.SYSTEM
                    currentLocales[0]?.language == "cs" -> AppLanguage.CZECH
                    currentLocales[0]?.language == "en" -> AppLanguage.ENGLISH
                    else -> AppLanguage.SYSTEM
                }

            viewModelScope.launch {
                appDataStore.appTheme.collect { theme ->
                    _state.value =
                        _state.value.copy(
                            selectedLanguage = currentLanguage,
                            selectedTheme = theme ?: AppTheme.SYSTEM,
                        )
                }
            }
        }

        fun selectLanguage(language: AppLanguage) {
            _state.value = _state.value.copy(selectedLanguage = language)

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
