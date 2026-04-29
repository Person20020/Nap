package com.github.person20020.nap.ui.screens

import android.media.AudioFocusRequest
import android.media.AudioManager
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import com.github.person20020.nap.constants.TitleBottomSpace
import com.github.person20020.nap.ui.components.ColumnWithContentPadding
import com.github.person20020.nap.ui.components.PreferenceEntry
import com.github.person20020.nap.ui.components.PreferenceGroupTitle
import com.github.person20020.nap.ui.components.ScreenTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperSettingsScreen() {
    ColumnWithContentPadding() {
        ScreenTitle(
            text = "Developer settings",
        )

        PreferenceGroupTitle(
            title = "Test functions",
        )
        ElevatedCard {
            // Pause media test
            val audioManager = getSystemService(LocalContext.current, AudioManager::class.java)
            PreferenceEntry(
                headlineContent = { Text("Pause media") },
                onClick = {
                    val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .build()

                    val result = audioManager?.requestAudioFocus(focusRequest)
                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        Log.d("Audio focus test", "Audio focus granted")
                        audioManager.abandonAudioFocusRequest(focusRequest)
                    } else {
                        Log.d("Audio focus test", "Audio focus request failed")
                    }
                },
            )
        }
    }
}