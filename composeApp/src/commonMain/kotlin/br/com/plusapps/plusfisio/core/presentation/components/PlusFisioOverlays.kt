package br.com.plusapps.plusfisio.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme

data class SheetAction(
    val label: String,
    val kind: PlusFisioButtonKind = PlusFisioButtonKind.Tertiary,
    val onClick: () -> Unit
)

@Composable
fun PlusFisioFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlusFisioButton(
        text = "+ $text",
        onClick = onClick,
        modifier = modifier,
        kind = PlusFisioButtonKind.Primary
    )
}

@Composable
fun PlusFisioBottomSheetCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.panel
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid5),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            Surface(
                color = PlusFisio.colors.line.copy(alpha = 0.8f),
                shape = PlusFisio.shapes.pill
            ) {
                Text(
                    text = "     ",
                    modifier = Modifier.padding(vertical = 1.dp),
                    color = PlusFisio.colors.line.copy(alpha = 0f)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            content()
        }
    }
}

@Composable
fun ConfirmationSheet(
    title: String,
    description: String,
    primaryAction: SheetAction,
    secondaryAction: SheetAction,
    modifier: Modifier = Modifier
) {
    PlusFisioBottomSheetCard(
        title = title,
        description = description,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)) {
            PlusFisioButton(
                text = primaryAction.label,
                onClick = primaryAction.onClick,
                kind = primaryAction.kind,
                modifier = Modifier.fillMaxWidth()
            )
            PlusFisioButton(
                text = secondaryAction.label,
                onClick = secondaryAction.onClick,
                kind = secondaryAction.kind,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ActionListSheet(
    title: String,
    description: String,
    actions: List<SheetAction>,
    modifier: Modifier = Modifier
) {
    PlusFisioBottomSheetCard(
        title = title,
        description = description,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)) {
            actions.forEach { action ->
                PlusFisioButton(
                    text = action.label,
                    onClick = action.onClick,
                    kind = action.kind,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun QuickFormSheet(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    footerAction: SheetAction
) {
    PlusFisioBottomSheetCard(
        title = title,
        description = description,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)) {
            content()
            PlusFisioButton(
                text = footerAction.label,
                onClick = footerAction.onClick,
                kind = footerAction.kind,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PlusFisioOverlaysPreview() {
    PlusFisioTheme {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            PlusFisioFloatingActionButton(
                text = "Novo agendamento",
                onClick = {}
            )
            ConfirmationSheet(
                title = "Confirmar atendimento",
                description = "Escolha a acao desejada para este atendimento.",
                primaryAction = SheetAction(label = "Editar", onClick = {}),
                secondaryAction = SheetAction(label = "Remarcar", onClick = {})
            )
            ActionListSheet(
                title = "Acoes do atendimento",
                description = "Escolha a acao desejada para este atendimento.",
                actions = listOf(
                    SheetAction(label = "Editar", onClick = {}),
                    SheetAction(label = "Remarcar", onClick = {}),
                    SheetAction(label = "Enviar via WhatsApp", onClick = {})
                )
            )
        }
    }
}
