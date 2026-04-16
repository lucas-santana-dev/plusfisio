package br.com.plusapps.plusfisio.features.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio

/**
 * Bloco de marca compartilhado da feature de autenticacao.
 */
@Composable
fun AuthBranding(
    badgeText: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    centered: Boolean = false,
    showBadge: Boolean = true
) {
    val horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    val textAlign = if (centered) TextAlign.Center else TextAlign.Start
    val spacing = PlusFisio.spacing

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = horizontalAlignment
    ) {
        if (showBadge) {
            Surface(
                color = PlusFisio.colors.surface.copy(alpha = 0.75f),
                shape = PlusFisio.shapes.pill
            ) {
                Text(
                    text = badgeText,
                    modifier = Modifier.padding(horizontal = spacing.grid4, vertical = spacing.grid3),
                    style = MaterialTheme.typography.titleMedium,
                    color = PlusFisio.colors.brand
                )
            }
            Spacer(modifier = Modifier.height(spacing.blockGap))
        }

        Text(
            text = title,
            style = PlusFisio.typeScale.display32,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = textAlign
        )
        Spacer(modifier = Modifier.height(spacing.grid3))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = textAlign
        )
    }
}
