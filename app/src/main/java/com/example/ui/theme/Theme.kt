package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PingVoltColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color(0xFF00382E),
    primaryContainer = Color(0xFF005143),
    onPrimaryContainer = Color(0xFF6FFFE4),

    secondary = ElectricBlue,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F58),
    onSecondaryContainer = Color(0xFF97F0FF),

    tertiary = NeonGreen,
    onTertiary = Color(0xFF00391F),
    tertiaryContainer = Color(0xFF00522F),
    onTertiaryContainer = Color(0xFF67FFAC),

    background = DeepNavyBg,
    onBackground = TextPrimary,
    surface = SurfaceCardBg,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PingVoltColorScheme,
        typography = Typography,
        content = content
    )
}
