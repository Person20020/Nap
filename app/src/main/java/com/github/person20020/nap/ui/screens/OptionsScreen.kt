package com.github.person20020.nap.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.person20020.nap.constants.TitleBottomSpace
import com.github.person20020.nap.ui.components.ColumnWithContentPadding
import com.github.person20020.nap.ui.components.ScreenTitle
import com.github.person20020.nap.ui.components.SliderPreference
import com.github.person20020.nap.ui.components.SwitchPreference
import com.github.person20020.nap.viewmodels.MainViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(
    mainViewModel: MainViewModel,
) {
    ColumnWithContentPadding(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(
            text = "Options",
        )

        ElevatedCard() {
            // Timer length slider
            val sliderState = rememberSliderState(
                value = 15f,
                valueRange = 1f..60f,
                steps = 58,
            )
            SliderPreference(
                headlineContent = { Text("Timer length") },
                valueDisplay = { Text("${sliderState.value.roundToInt()} minute${if (sliderState.value > 1) "s" else ""}") },
                sliderState = sliderState,
            )

            // Shake to reset
            var shakeToReset by remember { mutableStateOf(true) }
            SwitchPreference(
                headlineContent = { Text("Shake to reset") },
                isChecked = shakeToReset,
                onCheckedChange = {
                    shakeToReset = !shakeToReset
                },
            )

            // Vibrate before end
            var vibrateBeforeEnd by remember { mutableStateOf(false) }
            SwitchPreference(
                headlineContent = { Text("Vibrate before end") },
                isChecked = vibrateBeforeEnd,
                onCheckedChange = {
                    vibrateBeforeEnd = !vibrateBeforeEnd
                },
            )

            // Play quiet tone before end
            var playToneBeforeEnd by remember { mutableStateOf(false) }
            SwitchPreference(
                headlineContent = { Text("Play tone before end") },
                supportingContent = { Text("Play a quiet tone 1 minute before the timer ends") },
                isChecked = playToneBeforeEnd,
                onCheckedChange = {
                    playToneBeforeEnd = !playToneBeforeEnd
                },
            )
        }
    }
}