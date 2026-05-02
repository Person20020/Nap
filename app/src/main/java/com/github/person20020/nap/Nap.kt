package com.github.person20020.nap

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.person20020.nap.data.PreferencesRepository

class Nap : Application() {
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    val repository by lazy { PreferencesRepository(dataStore) }
    // TODO: Load preferences on app start (during splash screen) so that the initial value isn't visible on settings
}
