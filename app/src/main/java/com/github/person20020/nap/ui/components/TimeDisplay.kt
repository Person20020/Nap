package com.github.person20020.nap.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TimeDisplay(
    modifier: Modifier = Modifier,
    remainingTime: Long,
    totalTime: Long,
    paused: Boolean, // For icon state
    onPauseResume: () -> Unit = {},
    onReset: () -> Unit = {},
    strokeWidth: Dp = 12.dp,
    segmentGap: Dp = 6.dp,
) {
    val remainingColor = MaterialTheme.colorScheme.primary
    val elapsedColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = modifier,
    ) {
        Canvas(
            modifier =
                Modifier
                    .padding(strokeWidth / 2)
                    .fillMaxWidth()
                    .aspectRatio(1f),
        ) {
            val diameter = size.width

            val gapAngle = ((segmentGap.toPx() + strokeWidth.toPx()) / (diameter / 2f)) * (180f / Math.PI).toFloat()

            val remainingFraction = remainingTime / totalTime.toFloat()
            val elapsedSweep =
                -360 + (remainingFraction * 360f) +
                    if (remainingTime > 0) (gapAngle * 2) else 0f

            // Elapsed time arc with gap
            if (remainingFraction < 1f && elapsedSweep < 0f) {
                drawArc(
                    color = elapsedColor.copy(0.5f),
                    startAngle = -90f - if (remainingTime > 0) gapAngle else 0f,
                    sweepAngle = elapsedSweep,
                    useCenter = false,
                    size = Size(size.width, size.height),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                )
            }
            // Remaining time
            if (remainingFraction > 0f) {
                drawArc(
                    color = remainingColor,
                    startAngle = -90f,
                    sweepAngle = remainingFraction * 360f,
                    useCenter = false,
                    size = Size(size.width, size.height),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                )
            }
        }
        ProvideTextStyle(
            MaterialTheme.typography.displayLarge,
        ) {
            Text(
                text = "${(remainingTime / 60).toInt()}:${(remainingTime % 60).toInt().toString().padStart(2, '0')}",
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(y = (-24).dp),
            )
        }
        Row(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .offset(y = 54.dp),
            horizontalArrangement =
                androidx.compose.foundation.layout.Arrangement
                    .spacedBy(16.dp),
        ) {
            FilledIconButton(
                onClick = onPauseResume,
                modifier =
                    Modifier
                        .height(64.dp)
                        .aspectRatio(1.3f),
            ) {
                if (paused) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Resume",
                        modifier = Modifier.size(42.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Pause,
                        contentDescription = "Pause",
                        modifier = Modifier.size(42.dp),
                    )
                }
            }
            FilledIconButton(
                onClick = onReset,
                modifier =
                    Modifier
                        .height(64.dp)
                        .aspectRatio(1.3f),
            ) {
                Icon(
                    imageVector = Icons.Rounded.RestartAlt,
                    contentDescription = "Reset",
                    modifier = Modifier.size(42.dp),
                )
            }
        }
    }
}
