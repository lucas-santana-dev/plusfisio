package br.com.plusapps.plusfisio.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButtonKind
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTopAppBar
import br.com.plusapps.plusfisio.core.presentation.components.StatusChip
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.features.home.presentation.HomeAttentionBannerUi
import br.com.plusapps.plusfisio.features.home.presentation.HomeMetricCardTone
import br.com.plusapps.plusfisio.features.home.presentation.HomeMetricCardUi
import br.com.plusapps.plusfisio.features.home.presentation.HomeNextAppointmentUi
import br.com.plusapps.plusfisio.features.home.presentation.HomeSectionUi

@Composable
fun HomeHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    PlusFisioTopAppBar(
        title = title,
        modifier = modifier
    )
}

@Composable
fun HomeGreetingBlock(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        modifier = modifier.fillMaxWidth(),
        style = PlusFisio.typeScale.body16,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun NextAppointmentCard(
    appointment: HomeNextAppointmentUi?,
    emptyTitle: String,
    emptyDescription: String,
    primaryEmptyActionLabel: String,
    emptySecondaryActionLabel: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PlusFisio.colors.brandSoft.copy(alpha = 0.72f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        shape = PlusFisio.shapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
        ) {
            StatusChip(
                text = appointment?.chipLabel ?: "Próximo atendimento",
                tone = PlusFisioStatusTone.Info
            )
            Text(
                text = appointment?.title ?: emptyTitle,
                style = PlusFisio.typeScale.title24.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = appointment?.details ?: emptyDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
            ) {
                PlusFisioButton(
                    text = appointment?.primaryActionLabel ?: primaryEmptyActionLabel,
                    onClick = onPrimaryClick,
                    modifier = Modifier.weight(1f)
                )
                PlusFisioButton(
                    text = appointment?.secondaryActionLabel ?: emptySecondaryActionLabel,
                    onClick = { onSecondaryClick?.invoke() ?: Unit },
                    modifier = Modifier.weight(1f),
                    kind = PlusFisioButtonKind.Secondary
                )
            }
        }
    }
}

@Composable
fun HomeMetricCard(
    metric: HomeMetricCardUi,
    modifier: Modifier = Modifier
) {
    val containerColor = when (metric.tone) {
        HomeMetricCardTone.Default -> MaterialTheme.colorScheme.surface
        HomeMetricCardTone.Highlight -> PlusFisio.colors.brandSoft.copy(alpha = 0.72f)
    }
    val valueColor = when (metric.tone) {
        HomeMetricCardTone.Default -> MaterialTheme.colorScheme.onSurface
        HomeMetricCardTone.Highlight -> MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier,
        color = containerColor,
        shape = PlusFisio.shapes.control,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
        ) {
            Text(
                text = metric.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = metric.value,
                style = PlusFisio.typeScale.title24.copy(fontWeight = FontWeight.Bold),
                color = valueColor
            )
            Text(
                text = metric.subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun HomeAttentionBanner(
    banner: HomeAttentionBannerUi,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFFBF1DE),
        shape = PlusFisio.shapes.control
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PlusFisio.spacing.grid4, vertical = PlusFisio.spacing.grid4),
            horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(6.dp)
                    .clip(PlusFisio.shapes.circular)
                    .background(PlusFisio.colors.warning)
            )
            Text(
                text = banner.message,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFF9A6814)
            )
        }
    }
}

@Composable
fun HomeSectionCard(
    section: HomeSectionUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.control,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 96.dp)
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (section.isHighlighted) FontWeight.SemiBold else FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (section.badgeText != null) {
                    StatusChip(
                        text = section.badgeText,
                        tone = if (section.isHighlighted) PlusFisioStatusTone.Info else PlusFisioStatusTone.Neutral
                    )
                }
            }
            Text(
                text = section.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
