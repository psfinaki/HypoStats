package app.hypostats.domain

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import app.hypostats.ui.model.AppLanguage
import javax.inject.Inject

class AppCompatLanguageManager
    @Inject
    constructor() : LanguageManager {
        override fun getCurrentLanguage(): AppLanguage {
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            return when {
                currentLocales.isEmpty -> AppLanguage.SYSTEM
                currentLocales[0]?.language == "cs" -> AppLanguage.CZECH
                currentLocales[0]?.language == "en" -> AppLanguage.ENGLISH
                else -> AppLanguage.SYSTEM
            }
        }

        override fun setLanguage(language: AppLanguage) {
            val localeList =
                when (language) {
                    AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
                    AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
                    AppLanguage.CZECH -> LocaleListCompat.forLanguageTags("cs")
                }

            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }
