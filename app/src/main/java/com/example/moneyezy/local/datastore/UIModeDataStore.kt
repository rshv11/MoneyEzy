package com.example.moneyezy.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Singleton


abstract class PrefsDataStore(context: Context, fileName: String) {
    internal val dataStore: DataStore<androidx.datastore.preferences.core.Preferences> = context.createDataStore(fileName)

}

class UIModeDataStore(context: Context) :
    PrefsDataStore(
        context,
        PREF_FILE_UI_MODE
    ),
    UIModeImpl {

    // used to get the data from datastore
    override val uiMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: false
            uiMode
        }

    // used to save the ui preference to datastore
    override suspend fun saveToDataStore(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isNightMode
        }
    }

    companion object {
        private const val PREF_FILE_UI_MODE = "ui_mode_preference"
        private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
    }
}


@Singleton
interface UIModeImpl {
    val uiMode: Flow<Boolean>
    suspend fun saveToDataStore(isNightMode: Boolean)
}
