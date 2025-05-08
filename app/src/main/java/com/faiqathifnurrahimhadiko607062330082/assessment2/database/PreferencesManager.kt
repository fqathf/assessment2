package com.faiqathifnurrahimhadiko607062330082.assessment2.database

import android.content.Context

class PreferencesManager(context: Context) {
    private val dataStore = context.createDataStore(name = "settings")

    val themeSetting = dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "system"
    }

    suspend fun updateThemeSetting(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_key")
    }
}