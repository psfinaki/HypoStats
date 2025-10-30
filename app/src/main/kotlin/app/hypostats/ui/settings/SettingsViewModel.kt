package app.hypostats.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import app.hypostats.domain.BackupService
import app.hypostats.ui.model.AppLanguage
import app.hypostats.ui.model.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val backupService: BackupService,
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
            _state.value = SettingsUiState(selectedLanguage = currentLanguage)
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

        suspend fun exportBackup(directory: File): Result<File> {
            val backupFile = File(directory, "backup.json")
            return backupService.exportToFile(backupFile)
        }

        suspend fun importBackup(directory: File): Result<Unit> {
            val backupFile = File(directory, "backup.json")
            return backupService.importFromFile(backupFile)
        }
    }
