package com.github.person20020.nap.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

val hueColors =
    Array(360) { i ->
        Color.hsv(i.toFloat(), 1f, 1f)
    }

@Composable
fun HueSelector(
    modifier: Modifier = Modifier,
    initialHue: Float = 0f,
    onHueChanged: (Float) -> Unit,
    thickness: Dp = 12.dp,
    diameter: Dp = 300.dp,
    ringSelectionBufferDistance: Dp = 12.dp,
    selectorColor: Color = MaterialTheme.colorScheme.secondary,
    selectorDiameter: Dp = 32.dp,
    selectorShadow: Dp = 4.dp,
    showColorPatch: Boolean = true,
    colorPatchSize: Dp = 96.dp,
    colorPatchBorder: Dp = 0.dp,
    colorPatchBorderColor: Color = MaterialTheme.colorScheme.secondary,
    allowDragThroughRing: Boolean = true,
) {
    var selectedHue by remember { mutableFloatStateOf(initialHue) }

    LaunchedEffect(initialHue) {
        selectedHue = initialHue
    }

    var dragStartOnRing by remember { mutableStateOf(false) }
    var dragThroughRing by remember { mutableStateOf(false) }

    var center by remember { mutableStateOf(Offset.Zero) }
    var radius by remember { mutableFloatStateOf(0f) }

    val ringThicknessPx = with(LocalDensity.current) { thickness.toPx() }
    val ringSelectionBufferDistancePx = with(LocalDensity.current) { ringSelectionBufferDistance.toPx() }

    fun isOnRing(
        offset: Offset,
        ringThicknessPx: Float,
        ringSelectionBufferDistancePx: Float,
    ): Boolean {
        val dist = (offset - center).getDistance()
        return dist in
            (radius - ringThicknessPx - ringSelectionBufferDistancePx)..(radius + ringThicknessPx + ringSelectionBufferDistancePx)
    }

    fun angleFromOffset(offset: Offset): Float {
        val dx = offset.x - center.x
        val dy = offset.y - center.y
        val angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
        return (angle + 360f) % 360f
    }

    Canvas(
        modifier =
            modifier
                .aspectRatio(1f)
                .width(diameter + (thickness + (10.dp)))
                // .border(2.dp, MaterialTheme.colorScheme.secondary)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (isOnRing(offset, ringThicknessPx, ringSelectionBufferDistancePx)) {
                            val hue = angleFromOffset(offset)
                            onHueChanged(hue)
                            selectedHue = hue
                        }
                    }
                }.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartOnRing = isOnRing(offset, ringThicknessPx, ringSelectionBufferDistancePx)
                        },
                        onDrag = { change, _ ->
                            // Drag went through the selector ring
                            if (!dragThroughRing) {
                                dragThroughRing =
                                    isOnRing(
                                        change.position,
                                        ringThicknessPx,
                                        ringSelectionBufferDistancePx,
                                    )
                            }
                            if (dragStartOnRing or (allowDragThroughRing and dragThroughRing)) {
                                val hue = angleFromOffset(change.position)
                                onHueChanged(hue)
                                selectedHue = hue
                            }
                        },
                        onDragEnd = {
                            dragStartOnRing = false
                            dragThroughRing = false
                        },
                    )
                },
    ) {
        val selectorRingStrokeRadius = diameter.toPx() / 2 - ringThicknessPx / 2

        center = Offset(size.width / 2f, size.height / 2f)
        radius = diameter.toPx() / 2

        // Ring
        val sweepBrush =
            Brush.sweepGradient(
                colors = hueColors.toList(),
                center = center,
            )
        drawCircle(
            brush = sweepBrush,
            style = Stroke(width = ringThicknessPx),
            radius = selectorRingStrokeRadius,
            center = center,
        )

        // Color patch
        if (showColorPatch) {
            drawCircle(
                color = Color.hsv(selectedHue, 1f, 1f),
                radius = colorPatchSize.toPx() / 2,
            )
            // Outline
            if (colorPatchBorder > 0.dp) {
                drawCircle(
                    color = colorPatchBorderColor,
                    radius = colorPatchSize.toPx() / 2,
                    style = Stroke(width = colorPatchBorder.toPx()),
                )
            }
        }

        // Selector dot
        val selectedHueRads = selectedHue / 180 * PI.toFloat()
        val selectorDotCenter =
            center + Offset(cos(selectedHueRads) * (radius - ringThicknessPx / 2), sin(selectedHueRads) * (radius - ringThicknessPx / 2))
        // Shadow
        if (selectorShadow > 0.dp) {
            drawCircle(
                color = Color(0xAAFFFFFF), // TODO: use theme color with some transparency instead of hardcoded white
                radius = selectorDiameter.toPx() / 2,
                center = selectorDotCenter,
                style = Stroke(width = selectorShadow.toPx()),
            )
        }
        // Dot
        drawCircle(
            color = selectorColor,
            radius = selectorDiameter.toPx() / 2,
            center = selectorDotCenter,
        )
    }
}
