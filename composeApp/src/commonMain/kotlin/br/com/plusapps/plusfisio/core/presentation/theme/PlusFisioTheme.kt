package br.com.plusapps.plusfisio.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Tema base do app usando a foundation extraida do Figma como source of truth.
 */
@Composable
fun PlusFisioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val tokens = plusFisioTokens(darkTheme = darkTheme)

    MaterialTheme(
        colorScheme = if (darkTheme) PlusFisioDarkColorScheme else PlusFisioLightColorScheme,
        typography = plusFisioMaterialTypography(),
        shapes = plusFisioMaterialShapes()
    ) {
        CompositionLocalProvider(
            LocalPlusFisioTokens provides tokens,
            content = content
        )
    }
}
