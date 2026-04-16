package br.com.plusapps.plusfisio.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People

@Composable
fun PlusFisioTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationLabel: String? = null,
    onNavigationClick: (() -> Unit)? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shape = PlusFisio.shapes.control
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = PlusFisio.spacing.grid4,
                    vertical = PlusFisio.spacing.grid3
                ),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationLabel != null && onNavigationClick != null) {
                Text(
                    text = navigationLabel,
                    modifier = Modifier.clickable(onClick = onNavigationClick),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (actionLabel != null && onActionClick != null) {
                Text(
                    text = actionLabel,
                    modifier = Modifier.clickable(onClick = onActionClick),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun PlusFisioBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = PlusFisio.spacing.grid2,
                    vertical = PlusFisio.spacing.grid2
                ),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid1),
            content = content
        )
    }
}

@Composable
fun RowScope.PlusFisioBottomBarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeText: String? = null
) {
    val containerColor = if (selected) {
        PlusFisio.colors.brandSoft
    } else {
        Color.Transparent
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .weight(1f)
            .clickable(onClick = onClick),
        color = containerColor,
        shape = PlusFisio.shapes.control
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(
                    horizontal = PlusFisio.spacing.grid1,
                    vertical = PlusFisio.spacing.grid2
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid1)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = PlusFisio.spacing.grid1)
                    .size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                if (badgeText != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-6).dp),
                        color = PlusFisio.colors.warning.copy(alpha = 0.18f),
                        contentColor = PlusFisio.colors.warning,
                        shape = PlusFisio.shapes.pill
                    ) {
                        Text(
                            text = badgeText,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 9.sp)
                        )
                    }
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp),
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun PlusFisioNavigationPreview() {
    PlusFisioTheme {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            PlusFisioTopAppBar(
                title = "Clientes",
                navigationLabel = "<",
                onNavigationClick = {},
                actionLabel = "Buscar",
                onActionClick = {}
            )
            PlusFisioBottomBar {
                PlusFisioBottomBarItem(
                    label = "Home",
                    icon = Icons.Filled.Home,
                    selected = true,
                    onClick = {}
                )
                PlusFisioBottomBarItem(
                    label = "Agenda",
                    icon = Icons.Filled.CalendarMonth,
                    selected = false,
                    onClick = {}
                )
                PlusFisioBottomBarItem(
                    label = "Clientes",
                    icon = Icons.Filled.People,
                    selected = false,
                    onClick = {}
                )
                PlusFisioBottomBarItem(
                    label = "Financeiro",
                    icon = Icons.Filled.AccountBalanceWallet,
                    selected = false,
                    onClick = {}
                )
                PlusFisioBottomBarItem(
                    label = "Mais",
                    icon = Icons.Filled.MoreHoriz,
                    selected = false,
                    onClick = {},
                    badgeText = "3"
                )
            }
        }
    }
}
