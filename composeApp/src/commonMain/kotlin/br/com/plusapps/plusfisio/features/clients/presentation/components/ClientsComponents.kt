package br.com.plusapps.plusfisio.features.clients.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioBottomBar
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioBottomBarItem
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButtonKind
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.components.StatusChip
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme

enum class ClientsShellTab {
    Home,
    Agenda,
    Clients,
    Finance,
    More
}

data class ClientFilterChipUi(
    val label: String,
    val tone: PlusFisioStatusTone,
    val isSelected: Boolean
)

data class ClientListCardUi(
    val clientId: String,
    val initials: String,
    val name: String,
    val subtitle: String,
    val badgeText: String?,
    val badgeTone: PlusFisioStatusTone
)

data class ClientHistoryItemCardUi(
    val title: String,
    val description: String,
    val timestampLabel: String,
    val tone: PlusFisioStatusTone
)

@Composable
fun ClientsTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(PlusFisio.shapes.circular)
                        .clickable(onClick = onBackClick)
                        .padding(2.dp)
                )
            }
            Text(
                text = title,
                style = PlusFisio.typeScale.title20,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                modifier = Modifier.clickable(onClick = onActionClick),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun ClientsBottomBar(
    selectedTab: ClientsShellTab,
    onTabClick: (ClientsShellTab) -> Unit,
    modifier: Modifier = Modifier
) {
    PlusFisioBottomBar(modifier = modifier) {
        ClientsBottomBarItem(
            label = "Home",
            icon = Icons.Filled.Home,
            tab = ClientsShellTab.Home,
            selectedTab = selectedTab,
            onTabClick = onTabClick
        )
        ClientsBottomBarItem(
            label = "Agenda",
            icon = Icons.Filled.CalendarMonth,
            tab = ClientsShellTab.Agenda,
            selectedTab = selectedTab,
            onTabClick = onTabClick
        )
        ClientsBottomBarItem(
            label = "Clientes",
            icon = Icons.Filled.People,
            tab = ClientsShellTab.Clients,
            selectedTab = selectedTab,
            onTabClick = onTabClick
        )
        ClientsBottomBarItem(
            label = "Financeiro",
            icon = Icons.Filled.AccountBalanceWallet,
            tab = ClientsShellTab.Finance,
            selectedTab = selectedTab,
            onTabClick = onTabClick
        )
        ClientsBottomBarItem(
            label = "Mais",
            icon = Icons.Filled.MoreHoriz,
            tab = ClientsShellTab.More,
            selectedTab = selectedTab,
            onTabClick = onTabClick
        )
    }
}

@Composable
fun ClientsFilterChip(
    chip: ClientFilterChipUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (chip.isSelected) {
        Color.Transparent
    } else {
        PlusFisio.colors.line
    }

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = if (chip.isSelected) {
            chip.tone.containerColor()
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(1.dp, borderColor),
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
                    .clip(CircleShape)
                    .background(chip.tone.contentColor())
            )
            Text(
                text = chip.label,
                style = MaterialTheme.typography.labelMedium,
                color = if (chip.isSelected) chip.tone.contentColor() else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ClientListCard(
    ui: ClientListCardUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card,
        border = BorderStroke(1.dp, PlusFisio.colors.line.copy(alpha = 0.8f))
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
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PlusFisio.colors.brandSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ui.initials,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid1)
            ) {
                Text(
                    text = ui.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = ui.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
            ) {
                if (ui.badgeText != null) {
                    StatusChip(
                        text = ui.badgeText,
                        tone = ui.badgeTone
                    )
                }
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = ui.name,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ClientInfoCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        shape = PlusFisio.shapes.card,
        border = BorderStroke(1.dp, PlusFisio.colors.line.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
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

@Composable
fun ClientQuickActionRow(
    primaryLabel: String,
    primaryAction: () -> Unit,
    secondaryLabel: String,
    secondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    primaryKind: PlusFisioButtonKind = PlusFisioButtonKind.Primary,
    secondaryKind: PlusFisioButtonKind = PlusFisioButtonKind.Secondary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
    ) {
        PlusFisioButton(
            text = primaryLabel,
            onClick = primaryAction,
            modifier = Modifier.weight(1f),
            kind = primaryKind
        )
        PlusFisioButton(
            text = secondaryLabel,
            onClick = secondaryAction,
            modifier = Modifier.weight(1f),
            kind = secondaryKind
        )
    }
}

@Composable
fun ClientHistoryItemCard(
    item: ClientHistoryItemCardUi,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = item.tone.containerColor(),
        shape = PlusFisio.shapes.card,
        border = BorderStroke(1.dp, PlusFisio.colors.line.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.timestampLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RowScope.ClientsBottomBarItem(
    label: String,
    icon: ImageVector,
    tab: ClientsShellTab,
    selectedTab: ClientsShellTab,
    onTabClick: (ClientsShellTab) -> Unit
) {
    PlusFisioBottomBarItem(
        label = label,
        icon = icon,
        selected = tab == selectedTab,
        onClick = { onTabClick(tab) }
    )
}

@Composable
private fun PlusFisioStatusTone.containerColor(): Color {
    return when (this) {
        PlusFisioStatusTone.Info -> PlusFisio.colors.brandSoft
        PlusFisioStatusTone.Success -> PlusFisio.colors.success.copy(alpha = 0.14f)
        PlusFisioStatusTone.Warning -> PlusFisio.colors.warning.copy(alpha = 0.16f)
        PlusFisioStatusTone.Error -> PlusFisio.colors.error.copy(alpha = 0.16f)
        PlusFisioStatusTone.Neutral -> MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
private fun PlusFisioStatusTone.contentColor(): Color {
    return when (this) {
        PlusFisioStatusTone.Info -> MaterialTheme.colorScheme.primary
        PlusFisioStatusTone.Success -> PlusFisio.colors.success
        PlusFisioStatusTone.Warning -> PlusFisio.colors.warning
        PlusFisioStatusTone.Error -> PlusFisio.colors.error
        PlusFisioStatusTone.Neutral -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

@Preview
@Composable
private fun ClientsComponentsPreview() {
    PlusFisioTheme {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            ClientsTopBar(title = "Clientes", actionLabel = "Buscar", onActionClick = {})
            ClientsFilterChip(
                chip = ClientFilterChipUi(
                    label = "Pendencias",
                    tone = PlusFisioStatusTone.Warning,
                    isSelected = true
                ),
                onClick = {}
            )
            ClientListCard(
                ui = ClientListCardUi(
                    clientId = "1",
                    initials = "JM",
                    name = "Juliana Martins",
                    subtitle = "Cadastro pronto para novos agendamentos.",
                    badgeText = "Ativo",
                    badgeTone = PlusFisioStatusTone.Success
                ),
                onClick = {}
            )
            ClientInfoCard(
                title = "Resumo operacional",
                description = "Cliente ativo, com contato pronto para agenda e cobranca simples.",
                actionLabel = "Detalhes",
                onActionClick = {}
            )
            ClientHistoryItemCard(
                item = ClientHistoryItemCardUi(
                    title = "Cadastro atualizado",
                    description = "Dados principais revisados para manter a agenda organizada.",
                    timestampLabel = "Hoje",
                    tone = PlusFisioStatusTone.Info
                )
            )
            ClientsBottomBar(
                selectedTab = ClientsShellTab.Clients,
                onTabClick = {}
            )
        }
    }
}
