package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PingHistoryPoint
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceCardBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PingWaveformChart(
    pingHistory: List<PingHistoryPoint>,
    currentPing: Int,
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    val displayPoints = if (pingHistory.isEmpty() || !isConnected) {
        listOf(18, 19, 17, 18, 19, 18, 17, 18, 19, 18)
    } else {
        pingHistory.map { it.pingMs }
    }

    val minPing = displayPoints.minOrNull() ?: currentPing
    val maxPing = displayPoints.maxOrNull() ?: currentPing
    val avgPing = if (displayPoints.isNotEmpty()) displayPoints.average().toInt() else currentPing

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCardBg)
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("ping_waveform_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isConnected) NeonGreen else TextMuted)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE LATENCY WAVEFORM",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricMini("Min", "$minPing ms", NeonGreen)
                    MetricMini("Avg", "$avgPing ms", CyberCyan)
                    MetricMini("Max", "$maxPing ms", TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .testTag("ping_canvas")
            ) {
                if (displayPoints.size < 2) return@Canvas

                val width = size.width
                val height = size.height

                // Grid lines
                val gridY1 = height * 0.25f
                val gridY2 = height * 0.75f
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(0f, gridY1),
                    end = Offset(width, gridY1),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(0f, gridY2),
                    end = Offset(width, gridY2),
                    strokeWidth = 1f
                )

                val rangeMin = (minPing - 4).coerceAtLeast(1)
                val rangeMax = (maxPing + 4).coerceAtLeast(rangeMin + 5)
                val rangeDiff = (rangeMax - rangeMin).toFloat()

                val stepX = width / (displayPoints.size - 1)

                val path = Path()
                val fillPath = Path()

                displayPoints.forEachIndexed { index, ping ->
                    val x = index * stepX
                    val normalizedY = 1f - ((ping - rangeMin) / rangeDiff).coerceIn(0f, 1f)
                    val y = 8f + normalizedY * (height - 16f)

                    if (index == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, height)
                        fillPath.lineTo(x, y)
                    } else {
                        // Smooth bezier curve
                        val prevX = (index - 1) * stepX
                        val prevPing = displayPoints[index - 1]
                        val prevNormY = 1f - ((prevPing - rangeMin) / rangeDiff).coerceIn(0f, 1f)
                        val prevY = 8f + prevNormY * (height - 16f)

                        val cx = (prevX + x) / 2f
                        path.cubicTo(cx, prevY, cx, y, x, y)
                        fillPath.cubicTo(cx, prevY, cx, y, x, y)
                    }
                }

                val lastX = (displayPoints.size - 1) * stepX
                fillPath.lineTo(lastX, height)
                fillPath.close()

                val fillGradient = Brush.verticalGradient(
                    colors = listOf(
                        CyberCyan.copy(alpha = if (isConnected) 0.35f else 0.10f),
                        CyberCyan.copy(alpha = 0.0f)
                    )
                )

                drawPath(path = fillPath, brush = fillGradient)

                drawPath(
                    path = path,
                    color = if (isConnected) CyberCyan else TextSecondary,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Highlight latest point
                val lastPing = displayPoints.last()
                val lastNormY = 1f - ((lastPing - rangeMin) / rangeDiff).coerceIn(0f, 1f)
                val lastY = 8f + lastNormY * (height - 16f)

                drawCircle(
                    color = CyberCyan.copy(alpha = 0.3f),
                    radius = 8.dp.toPx(),
                    center = Offset(lastX, lastY)
                )
                drawCircle(
                    color = if (isConnected) NeonGreen else CyberCyan,
                    radius = 4.dp.toPx(),
                    center = Offset(lastX, lastY)
                )
            }
        }
    }
}

@Composable
private fun MetricMini(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontSize = 11.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}
