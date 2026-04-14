package br.com.plusapps.plusfisio.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF1F6F78),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCDEEEF),
    onPrimaryContainer = Color(0xFF10373B),
    secondary = Color(0xFF3E7A73),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7EFE7),
    onSecondaryContainer = Color(0xFF163833),
    tertiary = Color(0xFF87A9C7),
    onTertiary = Color(0xFF0F2236),
    background = Color(0xFFF6FBFA),
    onBackground = Color(0xFF1B2628),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B2628),
    surfaceVariant = Color(0xFFE6F0EE),
    onSurfaceVariant = Color(0xFF516062),
    outline = Color(0xFFB1C5C3),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8DD3D7),
    onPrimary = Color(0xFF00363B),
    primaryContainer = Color(0xFF114E54),
    onPrimaryContainer = Color(0xFFCDEEEF),
    secondary = Color(0xFFB3D4CC),
    onSecondary = Color(0xFF203833),
    secondaryContainer = Color(0xFF364E49),
    onSecondaryContainer = Color(0xFFD7EFE7),
    background = Color(0xFF101819),
    onBackground = Color(0xFFE0E7E6),
    surface = Color(0xFF152021),
    onSurface = Color(0xFFE0E7E6),
    surfaceVariant = Color(0xFF243132),
    onSurfaceVariant = Color(0xFFB9C9C8),
    outline = Color(0xFF869998),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 34.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.4).sp
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.2).sp
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.SemiBold
    )
)

private val AppShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
)

/**
 * Tema base do app para a primeira iteracao visual do PlusFisio.
 */
@Composable
fun PlusFisioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
