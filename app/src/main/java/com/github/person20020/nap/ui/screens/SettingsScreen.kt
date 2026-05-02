package com.github.person20020.nap.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.person20020.nap.constants.ElevatedCardSpacing
import com.github.person20020.nap.ui.components.ColumnWithContentPadding
import com.github.person20020.nap.ui.components.HueSaturationSelector
import com.github.person20020.nap.ui.components.ListDialogPreference
import com.github.person20020.nap.ui.components.MinimalDialog
import com.github.person20020.nap.ui.components.PreferenceEntry
import com.github.person20020.nap.ui.components.ScreenTitle
import com.github.person20020.nap.ui.components.SwitchPreference
import com.github.person20020.nap.ui.theme.DefaultSeedColor
import com.github.person20020.nap.utils.toHsl
import com.github.person20020.nap.viewmodels.MainViewModel

@Composable
fun SettingsScreen(
    mainViewModel: MainViewModel,
    onNavigate: (String) -> Unit,
) {
    val darkTheme by mainViewModel.darkTheme.collectAsStateWithLifecycle()
    val dynamicColors by mainViewModel.dynamicColors.collectAsStateWithLifecycle()
    val seedColor by mainViewModel.seedColor.collectAsStateWithLifecycle()

    val developerSettingsEnabled by mainViewModel.developerSettings.collectAsStateWithLifecycle()

    ColumnWithContentPadding(
        modifier = Modifier.fillMaxSize(),
    ) {
        ScreenTitle(
            text = "Settings",
        )

        ElevatedCard {
            // Dark theme
            ListDialogPreference(
                headlineContent = { Text("Dark theme") },
                supportingContent = {
                    when (darkTheme) {
                        -1 -> Text("Off")
                        1 -> Text("On")
                        else -> Text("Follow system")
                    }
                },
                trailingContent = {
                    Icon(
                        imageVector =
                            when (darkTheme) {
                                -1 -> Icons.Rounded.LightMode
                                1 -> Icons.Rounded.DarkMode
                                else -> Icons.Rounded.Contrast
                            },
                        contentDescription = null,
                    )
                },
                selectedIndex = darkTheme + 1,
                options = listOf("Off", "Follow system", "On"),
                onConfirm = {
                    mainViewModel.setDarkTheme(it - 1)
                },
            )

            // Dynamic/system colors
            SwitchPreference(
                headlineContent = { Text("System colors") },
                supportingContent = { Text("Use the system color scheme") },
                isChecked = dynamicColors,
                onCheckedChange = {
                    mainViewModel.setDynamicColors(!dynamicColors)
                },
            )

            // Color theme
            var showSeedColorDialog by remember { mutableStateOf(false) }
            AnimatedVisibility(
                visible = !dynamicColors,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                PreferenceEntry(
                    headlineContent = { Text("Color theme") },
                    supportingContent = { Text("App base color") },
                    trailingContent = {
                        Box(
                            modifier =
                                Modifier
                                    .background(
                                        Color.fromColorLong(seedColor),
                                        CircleShape,
                                    ).size(24.dp),
                        )
                    },
                    onClick = {
                        showSeedColorDialog = true
                    },
                )
            }
            var seedColorLong by remember(seedColor) { mutableLongStateOf(seedColor) }
            if (showSeedColorDialog) {
                MinimalDialog(
                    onDismissRequest = {
                        seedColorLong = seedColor
                        showSeedColorDialog = false
                    },
                    onConfirmButton = {
                        mainViewModel.setSeedColor(seedColorLong)
                        showSeedColorDialog = false
                    },
                    extraButtonText = "Reset",
                    onExtraButton = {
                        seedColorLong = DefaultSeedColor.toColorLong()
                    },
                ) {
                    BoxWithConstraints {
                        HueSaturationSelector(
                            onChange = { selectedHue, selectedSaturation ->
                                val selectedColor = Color.hsl(selectedHue, selectedSaturation, 0.5f)
                                seedColorLong = selectedColor.toColorLong()
                            },
                            initialHue = Color.fromColorLong(seedColorLong).toHsl().component1(),
                            initialSaturation = Color.fromColorLong(seedColorLong).toHsl().component2(),
                            maxSaturation = 1f,
                            minSaturation = 0.5f,
                            diameter = maxWidth - 32.dp,
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(ElevatedCardSpacing),
        )

        ElevatedCard {
            // Display remaining time (as a notification)
            var displayRemainingTime by remember { mutableStateOf(false) }
            SwitchPreference(
                headlineContent = { Text("Display remaining time") },
                supportingContent = { Text("Display the remaining time in a notification while the timer is active") },
                isChecked = displayRemainingTime,
                onCheckedChange = {
                    displayRemainingTime = !displayRemainingTime
                },
            )
        }

        Spacer(
            modifier = Modifier.height(ElevatedCardSpacing),
        )

        ElevatedCard {
            // Developer settings
            SwitchPreference(
                headlineContent = { Text("Show developer settings") },
                isChecked = developerSettingsEnabled,
                onCheckedChange = {
                    mainViewModel.setDeveloperSettings(!developerSettingsEnabled)
                },
            )
            // Developer settings entry hidden unless enabled
            AnimatedVisibility(
                visible = developerSettingsEnabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                Column {
                    PreferenceEntry(
                        headlineContent = { Text("Developer settings") },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            onNavigate("settings/developer")
                        },
                    )
                }
            }
        }
    }
}
