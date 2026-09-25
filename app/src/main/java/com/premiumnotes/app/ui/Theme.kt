package com.premiumnotes.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF315CFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3E9FF),
    secondary = Color(0xFF566175),
    background = Color(0xFFF8F9FC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE9ECF4),
    onSurface = Color(0xFF151923),
    onSurfaceVariant = Color(0xFF5F6675)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB5C4FF),
    onPrimary = Color(0xFF082276),
    primaryContainer = Color(0xFF1E3B9A),
    secondary = Color(0xFFBBC4DB),
    background = Color(0xFF101218),
    surface = Color(0xFF181A21),
    surfaceVariant = Color(0xFF292C35),
    onSurface = Color(0xFFE5E7EF),
    onSurfaceVariant = Color(0xFFBEC3D0)
)

@Composable
fun PremiumNotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
