package com.github.person20020.nap.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun NapTheme(
    seedColor: Color,
    isDark: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        MaterialTheme(
            colorScheme = if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context),
            typography = Typography,
            content = content,
        )
    } else {
        val dynamicThemeState =
            rememberDynamicMaterialThemeState(
                seedColor = seedColor,
                isDark = isDark,
                style = PaletteStyle.Content,
            )
        DynamicMaterialTheme(
            state = dynamicThemeState,
            animate = true,
            content = content,
        )
    }
}
