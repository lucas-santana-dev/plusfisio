package br.com.plusapps.plusfisio.features.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Background compartilhado das telas de autenticacao.
 *
 * Centraliza o gradiente e as formas decorativas para manter consistencia
 * visual entre splash, login e futuras telas da feature.
 */
@Composable
fun AuthBackground(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                    )
                )
            )
            .padding(contentPadding)
    ) {
        AuthDecoration(
            alignment = Alignment.TopStart,
            topPadding = 48.dp,
            horizontalPadding = 24.dp,
            size = 140.dp,
            rounded = false
        )
        AuthDecoration(
            alignment = Alignment.TopEnd,
            topPadding = 96.dp,
            horizontalPadding = 28.dp,
            size = 110.dp,
            rounded = true
        )
        content()
    }
}

@Composable
private fun BoxScope.AuthDecoration(
    alignment: Alignment,
    topPadding: Dp,
    horizontalPadding: Dp,
    size: Dp,
    rounded: Boolean
) {
    Box(
        modifier = Modifier
            .align(alignment)
            .padding(
                start = if (alignment == Alignment.TopStart) horizontalPadding else 0.dp,
                end = if (alignment == Alignment.TopEnd) horizontalPadding else 0.dp,
                top = topPadding
            )
            .size(size)
            .clip(if (rounded) RoundedCornerShape(34.dp) else CircleShape)
            .background(
                color = if (rounded) {
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f)
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                }
            )
    )
}
