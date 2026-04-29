package com.github.person20020.nap.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceEntry(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(
                    enabled = isEnabled,
                    onClick = onClick,
                ).alpha(if (isEnabled) 1f else 0.5f)
                .padding(16.dp),
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.padding(end = 4.dp),
            ) {
                icon()
            }

            Spacer(
                modifier = Modifier.width(12.dp),
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f),
        ) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                headlineContent()
            }
            if (supportingContent != null) {
                ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                    supportingContent()
                }
            }
        }

        if (trailingContent != null) {
            Spacer(
                modifier = Modifier.width(12.dp),
            )
            trailingContent()
        }
    }
}

@Composable
fun <T> ListDialogPreference(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    selectedIndex: Int,
    options: List<T>,
    icon: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
    onValueChange: ((Int) -> Unit)? = null,
    onConfirm: (Int) -> Unit,
) {
    var showListDialog by remember { mutableStateOf(false) }
    var selectedIndexVar by remember(selectedIndex) { mutableIntStateOf(selectedIndex) }
    PreferenceEntry(
        modifier = modifier,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = { (trailingContent ?: {})() },
        icon = icon,
        isEnabled = isEnabled,
        onClick = { showListDialog = true },
    )

    if (showListDialog) {
        MinimalDialog(
            onDismissRequest = {
                selectedIndexVar = selectedIndex
                showListDialog = false
            },
            onConfirmButton = {
                showListDialog = false
                onConfirm(selectedIndexVar)
            },
        ) {
            Column {
                options.forEachIndexed { index, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedIndexVar != index) {
                                        selectedIndexVar = index
                                        if (onValueChange != null) {
                                            onValueChange(index)
                                        }
                                    }
                                },
                    ) {
                        RadioButton(
                            selected = index == selectedIndexVar,
                            onClick = {
                                if (selectedIndexVar != index) {
                                    selectedIndexVar = index
                                    if (onValueChange != null) {
                                        onValueChange(index)
                                    }
                                }
                            },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(option.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
    isChecked: Boolean,
    onCheckedChange: (Boolean?) -> Unit,
) {
    PreferenceEntry(
        modifier = modifier,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        icon = icon,
        trailingContent = {
            Box(
                modifier =
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
            ) {
                Switch(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(it) },
                )
            }
        },
        isEnabled = isEnabled,
        onClick = { onCheckedChange(null) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderPreference(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    valueDisplay: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
    sliderState: SliderState,
    showTickMarks: Boolean = false,
) {
    PreferenceEntry(
        modifier = modifier,
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                headlineContent()
                if (valueDisplay != null) {
                    Spacer(
                        modifier = Modifier.weight(1f),
                    )
                    valueDisplay()
                }
            }
        },
        supportingContent = {
            Slider(
                modifier = Modifier,
                state = sliderState,
                enabled = isEnabled,
                colors = (
                    if (!showTickMarks) {
                        SliderDefaults.colors(
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent,
                        )
                    } else {
                        SliderDefaults.colors()
                    }
                ),
            )
            if (supportingContent != null) {
                ProvideTextStyle(
                    MaterialTheme.typography.bodySmall,
                ) {
                    supportingContent()
                }
            }
        },
        icon = icon,
        isEnabled = isEnabled,
        onClick = {},
    )
}

@Composable
fun PreferenceGroupTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}
