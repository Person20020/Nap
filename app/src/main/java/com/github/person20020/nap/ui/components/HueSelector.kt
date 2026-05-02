package com.github.person20020.nap.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

const val LIGHTNESS = 0.5f

private fun renderHueWheel(
    size: Int,
    ringThickness: Int,
    maxSaturation: Float = 1f,
    minSaturation: Float = 0f,
): ImageBitmap {
    val bitmap = createBitmap(size, size)
    val cx = size / 2f
    val cy = size / 2f
    val radius = size / 2f
    val innerRadius = (size / 2f) - ringThickness

    val outerRadiusSq = radius * radius
    val innerRadiusSq = innerRadius * innerRadius

    val pixels = IntArray(size * size)
    val hsl = floatArrayOf(0f, 1f, LIGHTNESS)

    for (y in 0 until size) {
        for (x in 0 until size) {
            val dx = x - cx
            val dy = y - cy
            val distSq = dx * dx + dy * dy

            if (distSq in innerRadiusSq..outerRadiusSq) {
                val angle =
                    Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                hsl[0] = (angle + 360f) % 360f

                // Map position to saturation based on min/max values and ring
                val currentRadius = sqrt(distSq)
                hsl[1] = minSaturation + ((currentRadius - innerRadius) / (size / 2f - innerRadius)) * (maxSaturation - minSaturation)

                hsl[2] = LIGHTNESS
                pixels[y * size + x] = Color.hsl(hsl[0], hsl[1], hsl[2]).toArgb()
            } else {
                pixels[y * size + x] = Color.Transparent.toArgb()
            }
        }
    }

    bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
    return bitmap.asImageBitmap()
}

@Composable
fun HueSaturationSelector(
    modifier: Modifier = Modifier,
    initialHue: Float = 0f,
    initialSaturation: Float = 1f,
    onChange: (hue: Float, saturation: Float) -> Unit,
    diameter: Dp = 300.dp,
    thickness: Dp = diameter / 4,
    maxSaturation: Float = 100f,
    minSaturation: Float = 0f,
    selectorColor: Color = MaterialTheme.colorScheme.primary,
    selectorDiameter: Dp = 32.dp,
    colorPatchSize: Dp = 0.dp,
) {
    val diameterPx = with(LocalDensity.current) { diameter.toPx().roundToInt() }
    val ringThicknessPx = with(LocalDensity.current) { thickness.toPx().roundToInt() }
    val innerDiameterPx = diameterPx - 2 * ringThicknessPx

    var selectedHue by remember { mutableFloatStateOf(initialHue) }
    var selectedSaturation by remember { mutableFloatStateOf(initialSaturation) }

    LaunchedEffect(initialHue, initialSaturation) {
        selectedHue = initialHue
        selectedSaturation = initialSaturation
    }

    val wheelBitmap by remember {
        mutableStateOf(
            renderHueWheel(
                size = diameterPx,
                ringThickness = ringThicknessPx,
                maxSaturation = maxSaturation,
                minSaturation = minSaturation,
            ),
        )
    }

    var center by remember { mutableStateOf(Offset.Zero) }

    fun pickFromOffset(offset: Offset) {
        val outerRadius = diameterPx / 2f
        val innerRadius = outerRadius - ringThicknessPx

        val dx = offset.x - center.x
        val dy = offset.y - center.y
        val dist =
            sqrt(dx * dx + dy * dy)
                .coerceAtMost(diameterPx / 2f)
                .coerceAtLeast(diameterPx / 2f - ringThicknessPx)

        val hue = (Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 360f) % 360f

        val saturation = minSaturation + ((dist - innerRadius) / (diameterPx / 2f - innerRadius)) * (maxSaturation - minSaturation)

        selectedHue = hue
        selectedSaturation = saturation
    }

    Canvas(
        modifier =
            modifier
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)

                        val dx = down.position.x - center.x
                        val dy = down.position.y - center.y
                        val dist = sqrt(dx * dx + dy * dy)

                        val outerRadius = diameterPx / 2f
                        val innerRadius = outerRadius - ringThicknessPx
                        val onRing = dist in innerRadius..outerRadius

                        if (onRing) {
                            down.consume()
                            pickFromOffset(down.position)
                            onChange(selectedHue, selectedSaturation)

                            do {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull() ?: break
                                if (change.pressed) {
                                    change.consume()
                                    pickFromOffset(change.position)
                                    onChange(selectedHue, selectedSaturation)
                                }
                            } while (event.changes.any { it.pressed })
                        }
                    }
                },
    ) {
        center = Offset(size.width / 2f, size.height / 2f)
        val outerRadius = diameterPx / 2f
        val innerRadius = outerRadius - ringThicknessPx

        // Color ring
        drawImage(
            image = wheelBitmap,
            dstOffset =
                IntOffset(
                    (center.x - outerRadius).toInt(),
                    (center.y - outerRadius).toInt(),
                ),
            dstSize = IntSize(diameterPx, diameterPx),
        )

        // Color patch
        drawCircle(
            color = Color.hsl(selectedHue, selectedSaturation, LIGHTNESS),
            radius = (outerRadius - ringThicknessPx) - 16.dp.toPx(), // TODO: Maybe make this a parameter
            center = center,
        )

        // Selector dot
        val hueRads = selectedHue / 180f * PI.toFloat()
        // linear map // output = out_min + (value - in_min) / (in_max - in_min) * (out_max - out_min)
        val selectorDist =
            innerRadius + (selectedSaturation - minSaturation) / (maxSaturation - minSaturation) * (outerRadius - innerRadius)
        val selectorCenter =
            center +
                Offset(
                    cos(hueRads) * selectorDist,
                    sin(hueRads) * selectorDist,
                )
        drawCircle(
            color = selectorColor,
            radius = selectorDiameter.toPx() / 2,
            center = selectorCenter,
        )
    }
}

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
                color = Color(0x80808080),
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
