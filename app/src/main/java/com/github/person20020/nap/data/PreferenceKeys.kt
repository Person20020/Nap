package com.github.person20020.nap.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object PreferenceKeys {
    // Appearance
    val DARK_THEME = intPreferencesKey("dark_theme")
    val DYNAMIC_COLORS = booleanPreferencesKey("dynamic_colors")
    val SEED_COLOR_HUE = floatPreferencesKey("seed_color_hue")

    // Timer options
    val TIMER_LENGTH = intPreferencesKey("timer_length")
    val TIMER_AUTO_ENABLE = booleanPreferencesKey("timer_auto_enable")
    val AUTO_ENABLE_TIME_START = longPreferencesKey("timer_auto_enable_start_time")
    val AUTO_ENABLE_TIME_END = longPreferencesKey("timer_auto_enable_end_time")

    val SHAKE_TO_RESET = booleanPreferencesKey("shake_to_reset")
    val VIBRATE_BEFORE_END = booleanPreferencesKey("vibrate_before_end")
    val TONE_BEFORE_END = booleanPreferencesKey("tone_before_end")

    // Other
    val ENABLE_PROGRESS_NOTIFICATION = booleanPreferencesKey("display_remaining_time_notification")
    val NOTIFICATION_PERMISSION_GRANTED = booleanPreferencesKey("notification_permission_granted")
    val DEVELOPER_SETTINGS = booleanPreferencesKey("developer_settings")
}
