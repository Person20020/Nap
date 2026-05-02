package com.github.person20020.nap.ui.screens

import android.media.AudioFocusRequest
import android.media.AudioManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.github.person20020.nap.constants.ElevatedCardSpacing
import com.github.person20020.nap.ui.components.ColumnWithContentPadding
import com.github.person20020.nap.ui.components.HueSaturationSelector
import com.github.person20020.nap.ui.components.HueSelector
import com.github.person20020.nap.ui.components.PreferenceEntry
import com.github.person20020.nap.ui.components.ScreenTitle
import com.github.person20020.nap.ui.components.TimeDisplay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperSettingsScreen() {
    ColumnWithContentPadding {
        // TODO: Add back button and make navbar settings go to correct page
        ScreenTitle(
            text = "Developer settings",
        )

        ElevatedCard {
            var showTestFunctions by remember { mutableStateOf(false) }
            PreferenceEntry(
                headlineContent = { Text("Test functions") },
                trailingContent = {
                    if (showTestFunctions) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(180f),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                        )
                    }
                },
                onClick = {
                    showTestFunctions = !showTestFunctions
                },
            )
            AnimatedVisibility(
                visible = showTestFunctions,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column {
                    // Pause media test
                    val audioManager = getSystemService(LocalContext.current, AudioManager::class.java)
                    PreferenceEntry(
                        headlineContent = { Text("Pause media") },
                        onClick = {
                            // Request audio focus
                            val focusRequest =
                                AudioFocusRequest
                                    .Builder(AudioManager.AUDIOFOCUS_GAIN)
                                    .build()

                            val result = audioManager?.requestAudioFocus(focusRequest)
                            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                                Log.d("Audio focus test", "Audio focus granted")
                                audioManager.abandonAudioFocusRequest(focusRequest)
                            } else {
                                Log.d("Audio focus test", "Audio focus request failed")
                            }

                            // Return audio focus
                            val abandonResult = audioManager?.abandonAudioFocusRequest(focusRequest)
                            if (abandonResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                                Log.d("Audio focus test", "Audio focus abandoned")
                            } else {
                                Log.d("Audio focus test", "Audio focus abandon failed")
                            }
                        },
                    )
                    PreferenceEntry(
                        headlineContent = { Text("Duck audio (5 sec)") },
                        onClick = {
                            // Request audio focus
                            val focusRequest =
                                AudioFocusRequest
                                    .Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                                    .build()

                            val result = audioManager?.requestAudioFocus(focusRequest)
                            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                                Log.d("Audio focus test", "Audio focus granted")
                                audioManager.abandonAudioFocusRequest(focusRequest)
                            } else {
                                Log.d("Audio focus test", "Audio focus request failed")
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                delay(5000)

                                // Return audio focus
                                val abandonResult = audioManager?.abandonAudioFocusRequest(focusRequest)
                                if (abandonResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                                    Log.d("Audio focus test", "Audio focus abandoned")
                                } else {
                                    Log.d("Audio focus test", "Audio focus abandon failed")
                                }
                            }
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(ElevatedCardSpacing))

        ElevatedCard {
            var showTestComponents by remember { mutableStateOf(false) }
            PreferenceEntry(
                headlineContent = { Text("Test components") },
                trailingContent = {
                    if (showTestComponents) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(180f),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                        )
                    }
                },
                onClick = {
                    showTestComponents = !showTestComponents
                },
            )

            AnimatedVisibility(
                visible = showTestComponents,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column {
                    val totalTime = 2 * 60
                    var remainingTime by remember { mutableIntStateOf(1 * 60 + 15) }
                    TimeDisplay(
                        modifier = Modifier.padding(24.dp),
                        remainingTime = remainingTime.toLong(),
                        totalTime = totalTime.toLong(),
                        paused = true,
                        onPauseResume = {
                            remainingTime = if (remainingTime >= 0) remainingTime - 1 else 0
                            if (remainingTime < 0) {
                                remainingTime = 0
                            }
                        },
                        onReset = {
                            remainingTime = totalTime
                        },
                    )

                    HueSelector(
                        modifier = Modifier.fillMaxWidth(),
                        initialHue = 0f,
                        onHueChanged = {},
                    )

                    BoxWithConstraints {
                        HueSaturationSelector(
                            modifier = Modifier.fillMaxWidth(),
                            diameter = maxWidth - 32.dp,
                            initialHue = 0f,
                            initialSaturation = 1f,
                            maxSaturation = 1f,
                            minSaturation = 0.5f,
                            onChange = { hue, saturation ->
                                // Log.d("HueSaturationSelector", "Hue: $hue, Saturation: $saturation")
                            },
                        )
                    }
                }
            }
        }
    }
}
