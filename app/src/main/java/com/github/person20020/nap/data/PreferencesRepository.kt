package com.github.person20020.nap.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.github.person20020.nap.ui.theme.DefaultSeedColor
import com.github.person20020.nap.utils.toHsv
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    // Reads
    val darkTheme: Flow<Int> = dataStore.data
        .map { it[PreferenceKeys.DARK_THEME] ?: 0 }
    val dynamicColors: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.DYNAMIC_COLORS] ?: false }
    val seedColorHue: Flow<Float> = dataStore.data
        .map { it[PreferenceKeys.SEED_COLOR_HUE] ?: DefaultSeedColor.toHsv()[0] }

    val timerLength: Flow<Int> = dataStore.data
        .map { it[PreferenceKeys.TIMER_LENGTH] ?: 15 }
    val timerAutoEnable: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.TIMER_AUTO_ENABLE] ?: false }
    val autoEnableTimeStart: Flow<Long> = dataStore.data
        .map { it[PreferenceKeys.AUTO_ENABLE_TIME_START] ?: (22 * 60 * 60).toLong() }
    val autoEnableTimeEnd: Flow<Long> = dataStore.data
        .map { it[PreferenceKeys.AUTO_ENABLE_TIME_END] ?: (6 * 60 * 60).toLong() }

    val shakeToReset: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.SHAKE_TO_RESET] ?: true }
    val vibrateBeforeEnd: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.VIBRATE_BEFORE_END] ?: false }
    val toneBeforeEnd: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.TONE_BEFORE_END] ?: false }

    val enableProgressNotification: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.ENABLE_PROGRESS_NOTIFICATION] ?: false }
    val notificationPermissionGranted: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.NOTIFICATION_PERMISSION_GRANTED] ?: false }
    val developerSettings: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.DEVELOPER_SETTINGS] ?: false }


    // Writes
    suspend fun setDarkTheme(value: Int) {
        dataStore.edit { it[PreferenceKeys.DARK_THEME] = value }
    }
    suspend fun setDynamicColors(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.DYNAMIC_COLORS] = value }
    }
    suspend fun setSeedColor(value: Float) {
        dataStore.edit { it[PreferenceKeys.SEED_COLOR_HUE] = value }
    }

    suspend fun setTimerLength(value: Int) {
        dataStore.edit { it[PreferenceKeys.TIMER_LENGTH] = value }
    }
    suspend fun setTimerAutoEnable(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.TIMER_AUTO_ENABLE] = value }
    }
    suspend fun setAutoEnableTimeStart(value: Long) {
        dataStore.edit { it[PreferenceKeys.AUTO_ENABLE_TIME_START] = value }
    }
    suspend fun setAutoEnableTimeEnd(value: Long) {
        dataStore.edit { it[PreferenceKeys.AUTO_ENABLE_TIME_END] = value }
    }

    suspend fun setShakeToReset(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.SHAKE_TO_RESET] = value }
    }
    suspend fun setVibrateBeforeEnd(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.VIBRATE_BEFORE_END] = value }
    }
    suspend fun setToneBeforeEnd(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.TONE_BEFORE_END] = value }
    }

    suspend fun setEnableProgressNotification(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.ENABLE_PROGRESS_NOTIFICATION] = value }
    }
    suspend fun setNotificationPermissionGranted(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.NOTIFICATION_PERMISSION_GRANTED] = value }
    }
    suspend fun setDeveloperSettings(value: Boolean) {
        dataStore.edit { it[PreferenceKeys.DEVELOPER_SETTINGS] = value }
    }

}