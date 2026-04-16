package br.com.plusapps.plusfisio.core.presentation.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.inter_bold
import plusfisio.composeapp.generated.resources.inter_medium
import plusfisio.composeapp.generated.resources.inter_regular
import plusfisio.composeapp.generated.resources.inter_semibold

@Immutable
data class PlusFisioColorTokens(
    val brand: Color,
    val brandSoft: Color,
    val ink: Color,
    val canvas: Color,
    val surface: Color,
    val line: Color,
    val success: Color,
    val warning: Color,
    val error: Color
)

@Immutable
data class PlusFisioSpacingTokens(
    val grid1: Dp = 4.dp,
    val grid2: Dp = 8.dp,
    val grid3: Dp = 12.dp,
    val grid4: Dp = 16.dp,
    val grid5: Dp = 20.dp,
    val grid6: Dp = 24.dp,
    val grid7: Dp = 28.dp,
    val grid8: Dp = 32.dp,
    val grid10: Dp = 40.dp,
    val grid12: Dp = 48.dp,
    val grid13: Dp = 52.dp,
    val screenHorizontal: Dp = 16.dp,
    val screenVertical: Dp = 24.dp,
    val sectionGap: Dp = 20.dp,
    val blockGap: Dp = 16.dp,
    val contentGap: Dp = 14.dp,
    val compactGap: Dp = 8.dp,
    val touchTarget: Dp = 48.dp,
    val controlHeight: Dp = 52.dp
)

@Immutable
data class PlusFisioShapeTokens(
    val swatch: Shape,
    val control: Shape,
    val card: Shape,
    val panel: Shape,
    val pill: Shape,
    val circular: Shape
)

@Immutable
data class PlusFisioTypeScale(
    val display32: TextStyle,
    val title24: TextStyle,
    val title20: TextStyle,
    val title18: TextStyle,
    val body16: TextStyle,
    val body14: TextStyle,
    val label13: TextStyle
)

@Immutable
data class PlusFisioTokens(
    val colors: PlusFisioColorTokens,
    val spacing: PlusFisioSpacingTokens,
    val shapes: PlusFisioShapeTokens,
    val typeScale: PlusFisioTypeScale
)

private val LightColors = PlusFisioColorTokens(
    brand = Color(0xFF2B7A78),
    brandSoft = Color(0xFFEAF7F5),
    ink = Color(0xFF10212B),
    canvas = Color(0xFFF6F8F7),
    surface = Color(0xFFFFFFFF),
    line = Color(0xFFD7E2E0),
    success = Color(0xFF23805A),
    warning = Color(0xFFB7791F),
    error = Color(0xFFC7524A)
)

private val DarkColors = PlusFisioColorTokens(
    brand = Color(0xFF5CB0AC),
    brandSoft = Color(0xFF173230),
    ink = Color(0xFFE8F0EE),
    canvas = Color(0xFF0F1716),
    surface = Color(0xFF16211F),
    line = Color(0xFF30413E),
    success = Color(0xFF44A176),
    warning = Color(0xFFD49A44),
    error = Color(0xFFE17C71)
)

private val BaseSpacing = PlusFisioSpacingTokens()

private val BaseShapes = PlusFisioShapeTokens(
    swatch = RoundedCornerShape(18.dp),
    control = RoundedCornerShape(20.dp),
    card = RoundedCornerShape(24.dp),
    panel = RoundedCornerShape(28.dp),
    pill = RoundedCornerShape(percent = 50),
    circular = CircleShape
)

internal val LocalPlusFisioTokens = staticCompositionLocalOf<PlusFisioTokens> {
    error("PlusFisio tokens not provided")
}

val PlusFisioLightColorScheme: ColorScheme = lightColorScheme(
    primary = LightColors.brand,
    onPrimary = LightColors.surface,
    primaryContainer = LightColors.brandSoft,
    onPrimaryContainer = LightColors.ink,
    secondary = LightColors.ink,
    onSecondary = LightColors.surface,
    secondaryContainer = LightColors.brandSoft,
    onSecondaryContainer = LightColors.ink,
    tertiary = LightColors.success,
    onTertiary = LightColors.surface,
    background = LightColors.canvas,
    onBackground = LightColors.ink,
    surface = LightColors.surface,
    onSurface = LightColors.ink,
    surfaceVariant = LightColors.brandSoft,
    onSurfaceVariant = Color(0xFF40535C),
    outline = LightColors.line,
    error = LightColors.error,
    onError = LightColors.surface
)

val PlusFisioDarkColorScheme: ColorScheme = darkColorScheme(
    primary = DarkColors.brand,
    onPrimary = DarkColors.canvas,
    primaryContainer = DarkColors.brandSoft,
    onPrimaryContainer = DarkColors.ink,
    secondary = DarkColors.ink,
    onSecondary = DarkColors.canvas,
    secondaryContainer = DarkColors.brandSoft,
    onSecondaryContainer = DarkColors.ink,
    tertiary = DarkColors.success,
    onTertiary = DarkColors.canvas,
    background = DarkColors.canvas,
    onBackground = DarkColors.ink,
    surface = DarkColors.surface,
    onSurface = DarkColors.ink,
    surfaceVariant = DarkColors.brandSoft,
    onSurfaceVariant = Color(0xFFB8C8C5),
    outline = DarkColors.line,
    error = DarkColors.error,
    onError = DarkColors.canvas
)

@Composable
private fun plusFisioFontFamily(): ComposeFontFamily = ComposeFontFamily(
    Font(Res.font.inter_regular, weight = FontWeight.Normal),
    Font(Res.font.inter_medium, weight = FontWeight.Medium),
    Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.inter_bold, weight = FontWeight.Bold)
)

@Composable
private fun plusFisioTypeScale(): PlusFisioTypeScale {
    val fontFamily = plusFisioFontFamily()

    return PlusFisioTypeScale(
        display32 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 32.sp,
            lineHeight = 43.sp,
            fontWeight = FontWeight.Bold
        ),
        title24 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.SemiBold
        ),
        title20 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 20.sp,
            lineHeight = 27.sp,
            fontWeight = FontWeight.SemiBold
        ),
        title18 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold
        ),
        body16 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal
        ),
        body14 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 14.sp,
            lineHeight = 19.sp,
            fontWeight = FontWeight.Normal
        ),
        label13 = TextStyle(
            fontFamily = fontFamily,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium
        )
    )
}

@Composable
fun plusFisioMaterialTypography(): Typography {
    val typeScale = plusFisioTypeScale()

    return Typography(
        displayLarge = typeScale.display32,
        headlineLarge = typeScale.title24,
        headlineMedium = typeScale.title20,
        titleLarge = typeScale.title18,
        titleMedium = typeScale.body16.copy(fontWeight = FontWeight.Medium),
        bodyLarge = typeScale.body16,
        bodyMedium = typeScale.body14,
        labelLarge = typeScale.label13,
        labelMedium = typeScale.body14.copy(fontSize = 12.sp, lineHeight = 16.sp)
    )
}

fun plusFisioMaterialShapes(): Shapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(18.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(28.dp)
)

@Composable
fun plusFisioTokens(darkTheme: Boolean): PlusFisioTokens = PlusFisioTokens(
    colors = if (darkTheme) DarkColors else LightColors,
    spacing = BaseSpacing,
    shapes = BaseShapes,
    typeScale = plusFisioTypeScale()
)

object PlusFisio {
    val colors: PlusFisioColorTokens
        @Composable get() = LocalPlusFisioTokens.current.colors

    val spacing: PlusFisioSpacingTokens
        @Composable get() = LocalPlusFisioTokens.current.spacing

    val shapes: PlusFisioShapeTokens
        @Composable get() = LocalPlusFisioTokens.current.shapes

    val typeScale: PlusFisioTypeScale
        @Composable get() = LocalPlusFisioTokens.current.typeScale
}
