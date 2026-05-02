package com.github.person20020.nap.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.person20020.nap.ui.components.ColumnWithContentPadding
import com.github.person20020.nap.ui.components.TimeDisplay
import com.github.person20020.nap.viewmodels.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    ColumnWithContentPadding(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var enabled by remember { mutableStateOf(true) }
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = enabled,
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2,
                    ),
                icon = {},
                onClick = {
                    enabled = true
                },
            ) {
                Text("Enabled")
            }
            SegmentedButton(
                selected = !enabled,
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = 2,
                    ),
                icon = {},
                onClick = {
                    enabled = false
                },
            ) {
                Text("Disabled")
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))
        val totalTime = 2 * 60
        var remainingTime by remember { mutableIntStateOf(1 * 60 + 15) }
        var paused by remember { mutableStateOf(false) }
        TimeDisplay(
            remainingTime = remainingTime.toLong(),
            totalTime = totalTime.toLong(),
            paused = paused,
            onPauseResume = {
                remainingTime = if (remainingTime >= 15) remainingTime - 15 else 0
                paused = !paused
            },
            onReset = {
                remainingTime = totalTime
            },
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
