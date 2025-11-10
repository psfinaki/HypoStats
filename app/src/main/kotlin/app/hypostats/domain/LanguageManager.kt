package app.hypostats.domain

import app.hypostats.ui.model.AppLanguage

interface LanguageManager {
    fun getCurrentLanguage(): AppLanguage

    fun setLanguage(language: AppLanguage)
}
