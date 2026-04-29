package com.github.person20020.nap.ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.person20020.nap.constants.TitleBottomSpace
import com.github.person20020.nap.ui.components.ColumnWithContentPadding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperSettingsScreen() {
    ColumnWithContentPadding() {
        ProvideTextStyle(MaterialTheme.typography.titleLarge) {
            Text("Developer Settings")
            Spacer(
                modifier = Modifier.height(TitleBottomSpace)
            )
        }
        ElevatedCard() {
            Text("hi")
        }
    }
}