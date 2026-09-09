package com.project.prayerreminder.utils.enumeration

enum class AppLanguage(
    val languageTag: String,
) {
    English(languageTag = "en"),
    Indonesian(languageTag = "id");

    companion object {
        fun fromLanguageTag(languageTag: String): AppLanguage {
            return entries.firstOrNull { language ->
                language.languageTag == languageTag
            } ?: English
        }
    }
}