package com.github.person20020.nap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.person20020.nap.Nap
import com.github.person20020.nap.ui.components.ColumnWithContentPadding
import com.github.person20020.nap.ui.components.HueSelector
import com.github.person20020.nap.ui.components.ScreenTitle
import com.github.person20020.nap.viewmodels.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val seedColorHue by mainViewModel.seedColorHue.collectAsStateWithLifecycle()

    ColumnWithContentPadding(
        modifier = Modifier.fillMaxSize(),
    ) {
        ScreenTitle(
            text = "Home",
        )

        HueSelector(
            modifier =
                Modifier
                    .padding(vertical = 16.dp),
            onHueChanged = {
                mainViewModel.setSeedColorHue(it)
                // Log.d("HueSelector", "Selected hue: ${it.toString()}")
            },
            initialHue = seedColorHue,
            diameter = 300.dp,
            colorPatchBorder = 2.dp,
        )

        // Seed color sample
        Box(
            modifier =
                Modifier
                    .background(color = Color.hsv(seedColorHue, 1f, 1f), shape = RoundedCornerShape(25))
                    .padding(32.dp),
        )
    }
}
