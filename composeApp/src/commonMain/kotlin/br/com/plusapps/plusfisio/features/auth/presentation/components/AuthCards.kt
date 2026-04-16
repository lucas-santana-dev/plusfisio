package br.com.plusapps.plusfisio.features.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio

/**
 * Card base do fluxo de autenticacao.
 */
@Composable
fun AuthPrimaryCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = PlusFisio.shapes.panel,
        shadowElevation = 10.dp,
        color = PlusFisio.colors.surface
    ) {
        content()
    }
}

/**
 * Card secundario para pequenas mensagens de apoio.
 */
@Composable
fun AuthHighlightCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PlusFisio.colors.brandSoft,
        shape = PlusFisio.shapes.control
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = PlusFisio.colors.ink
            )
            Text(
                text = description,
                modifier = Modifier.padding(top = PlusFisio.spacing.grid2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
