package com.github.person20020.nap

import android.app.Application
import android.preference.PreferenceDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.person20020.nap.data.PreferencesRepository

class Nap : Application() {
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    val repository by lazy { PreferencesRepository(dataStore) }
}