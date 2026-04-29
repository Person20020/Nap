package com.github.person20020.nap.utils

import android.graphics.Color.colorToHSV
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toHsv(): FloatArray {
    val hsv = FloatArray(3)
    colorToHSV(this.toArgb(), hsv)
    return hsv
}