package br.com.plusapps.plusfisio.features.auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio

@Composable
fun InitialFlowTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(PlusFisio.spacing.grid12 + PlusFisio.spacing.grid2),
        horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onBackClick),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shape = PlusFisio.shapes.pill
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\u2190",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
