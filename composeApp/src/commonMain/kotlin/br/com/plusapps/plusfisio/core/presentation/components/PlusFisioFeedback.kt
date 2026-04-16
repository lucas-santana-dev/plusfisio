package br.com.plusapps.plusfisio.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme

enum class PlusFisioStatusTone {
    Info,
    Success,
    Warning,
    Error,
    Neutral
}

@Composable
fun StatusChip(
    text: String,
    tone: PlusFisioStatusTone,
    modifier: Modifier = Modifier
) {
    val colors = chipColors(tone)
    Surface(
        modifier = modifier,
        color = colors.container,
        contentColor = colors.content,
        shape = PlusFisio.shapes.pill
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = PlusFisio.spacing.grid3,
                vertical = PlusFisio.spacing.grid2
            ),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color = colors.content, shape = CircleShape)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun FeedbackBanner(
    title: String,
    tone: PlusFisioStatusTone,
    modifier: Modifier = Modifier,
    message: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    val colors = chipColors(tone)
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.container.copy(alpha = 0.72f),
        contentColor = colors.content,
        shape = PlusFisio.shapes.control
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = colors.content, shape = CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.content
                )
                if (message != null) {
                    Spacer(modifier = Modifier.height(PlusFisio.spacing.grid1))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.content.copy(alpha = 0.9f)
                    )
                }
            }
            if (actionLabel != null && onActionClick != null) {
                PlusFisioButton(
                    text = actionLabel,
                    onClick = onActionClick,
                    kind = PlusFisioButtonKind.Tertiary
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid5),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = PlusFisio.colors.brandSoft,
                        shape = PlusFisio.shapes.circular
                    )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (actionLabel != null && onActionClick != null) {
                PlusFisioButton(
                    text = actionLabel,
                    onClick = onActionClick,
                    kind = PlusFisioButtonKind.Secondary
                )
            }
        }
    }
}

private data class ToneColors(
    val container: Color,
    val content: Color
)

@Composable
private fun chipColors(tone: PlusFisioStatusTone): ToneColors = when (tone) {
    PlusFisioStatusTone.Info -> ToneColors(
        container = PlusFisio.colors.brandSoft,
        content = MaterialTheme.colorScheme.primary
    )

    PlusFisioStatusTone.Success -> ToneColors(
        container = PlusFisio.colors.success.copy(alpha = 0.14f),
        content = PlusFisio.colors.success
    )

    PlusFisioStatusTone.Warning -> ToneColors(
        container = PlusFisio.colors.warning.copy(alpha = 0.16f),
        content = PlusFisio.colors.warning
    )

    PlusFisioStatusTone.Error -> ToneColors(
        container = PlusFisio.colors.error.copy(alpha = 0.16f),
        content = PlusFisio.colors.error
    )

    PlusFisioStatusTone.Neutral -> ToneColors(
        container = MaterialTheme.colorScheme.surfaceVariant,
        content = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview
@Composable
private fun PlusFisioFeedbackPreview() {
    PlusFisioTheme {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)) {
                StatusChip(text = "Lembrete", tone = PlusFisioStatusTone.Info)
                StatusChip(text = "Falta", tone = PlusFisioStatusTone.Error)
                StatusChip(text = "Atendido", tone = PlusFisioStatusTone.Success)
            }
            FeedbackBanner(
                title = "Todos os atendimentos da manha foram confirmados",
                tone = PlusFisioStatusTone.Success
            )
            EmptyState(
                title = "Nenhum atendimento neste periodo",
                description = "Crie um novo agendamento ou use a agenda semanal para planejar a proxima sequencia.",
                actionLabel = "Novo agendamento",
                onActionClick = {}
            )
        }
    }
}
