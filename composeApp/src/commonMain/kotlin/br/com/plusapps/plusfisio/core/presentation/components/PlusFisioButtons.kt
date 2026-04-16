package br.com.plusapps.plusfisio.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme

enum class PlusFisioButtonKind {
    Primary,
    Secondary,
    Tertiary,
    Destructive
}

@Composable
fun PlusFisioButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    kind: PlusFisioButtonKind = PlusFisioButtonKind.Primary,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    when (kind) {
        PlusFisioButtonKind.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .defaultMinSize(minHeight = PlusFisio.spacing.controlHeight)
                    .height(PlusFisio.spacing.controlHeight),
                enabled = enabled && !loading,
                shape = PlusFisio.shapes.control,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                PlusFisioButtonContent(
                    text = text,
                    loading = loading,
                    progressColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        PlusFisioButtonKind.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .defaultMinSize(minHeight = PlusFisio.spacing.controlHeight)
                    .height(PlusFisio.spacing.controlHeight),
                enabled = enabled && !loading,
                shape = PlusFisio.shapes.control,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                PlusFisioButtonContent(
                    text = text,
                    loading = loading,
                    progressColor = MaterialTheme.colorScheme.primary
                )
            }
        }

        PlusFisioButtonKind.Tertiary -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.defaultMinSize(minHeight = 40.dp),
                enabled = enabled && !loading,
                shape = PlusFisio.shapes.pill,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                PlusFisioButtonContent(
                    text = text,
                    loading = loading,
                    progressColor = MaterialTheme.colorScheme.primary
                )
            }
        }

        PlusFisioButtonKind.Destructive -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .defaultMinSize(minHeight = PlusFisio.spacing.controlHeight)
                    .height(PlusFisio.spacing.controlHeight),
                enabled = enabled && !loading,
                shape = PlusFisio.shapes.control,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PlusFisio.colors.error.copy(alpha = 0.16f),
                    contentColor = PlusFisio.colors.error,
                    disabledContainerColor = PlusFisio.colors.error.copy(alpha = 0.08f),
                    disabledContentColor = PlusFisio.colors.error.copy(alpha = 0.45f)
                )
            ) {
                PlusFisioButtonContent(
                    text = text,
                    loading = loading,
                    progressColor = PlusFisio.colors.error
                )
            }
        }
    }
}

@Composable
fun PlusFisioIconButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(44.dp),
        enabled = enabled,
        shape = PlusFisio.shapes.circular,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun PlusFisioButtonContent(
    text: String,
    loading: Boolean,
    progressColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 0.dp),
                color = progressColor,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
private fun PlusFisioButtonsPreview() {
    PlusFisioTheme {
        Row(
            modifier = Modifier.padding(PlusFisio.spacing.grid4),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
        ) {
            PlusFisioButton(text = "Salvar", onClick = {})
            PlusFisioButton(
                text = "Voltar",
                onClick = {},
                kind = PlusFisioButtonKind.Secondary
            )
            PlusFisioButton(
                text = "Cancelar",
                onClick = {},
                kind = PlusFisioButtonKind.Destructive
            )
            PlusFisioIconButton(label = "+", onClick = {})
        }
    }
}
