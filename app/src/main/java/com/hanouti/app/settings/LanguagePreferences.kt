package com.hanouti.app.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.core.os.LocaleListCompat
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "settings")

object LanguagePreferences {
    private val KEY_APP_LANGUAGE: Preferences.Key<String> = stringPreferencesKey("app_language")

    suspend fun setLanguage(context: Context, languageTag: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_APP_LANGUAGE] = languageTag
        }
        applyLocale(languageTag)
    }

    suspend fun getLanguage(context: Context): String {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_APP_LANGUAGE] ?: "en"
    }

    fun applyLocale(languageTag: String) {
        val locales = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}


