package br.com.plusapps.plusfisio.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme

enum class SummaryCardTone {
    Default,
    Highlight,
    Alert
}

enum class AppointmentItemStatus {
    Scheduled,
    Confirmed,
    Attended,
    Missed
}

enum class PaymentItemStatus {
    Pending,
    DueSoon,
    Paid
}

enum class PackageSessionStatus {
    Active,
    LowBalance,
    Expired
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    tone: SummaryCardTone = SummaryCardTone.Default
) {
    val colors = summaryColors(tone)
    Surface(
        modifier = modifier,
        color = colors.container,
        shape = PlusFisio.shapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = colors.secondary
            )
            Text(
                text = value,
                style = PlusFisio.typeScale.title24.copy(fontWeight = FontWeight.Bold),
                color = colors.primary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.secondary
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    expanded: Boolean = false,
    onToggleClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.control
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (badgeText != null) {
                    StatusChip(
                        text = badgeText,
                        tone = PlusFisioStatusTone.Warning
                    )
                }
                if (onToggleClick != null) {
                    PlusFisioButton(
                        text = if (expanded) "-" else "+",
                        onClick = onToggleClick,
                        kind = PlusFisioButtonKind.Tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentItem(
    timeLabel: String,
    clientName: String,
    details: String,
    status: AppointmentItemStatus,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryActionClick: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryActionClick: (() -> Unit)? = null,
    tertiaryActionLabel: String? = null,
    onTertiaryActionClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$timeLabel - $clientName",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                StatusChip(
                    text = appointmentStatusLabel(status),
                    tone = appointmentStatusTone(status)
                )
            }
            Text(
                text = details,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
            ) {
                if (primaryActionLabel != null && onPrimaryActionClick != null) {
                    PlusFisioButton(
                        text = primaryActionLabel,
                        onClick = onPrimaryActionClick,
                        kind = PlusFisioButtonKind.Tertiary
                    )
                }
                if (secondaryActionLabel != null && onSecondaryActionClick != null) {
                    PlusFisioButton(
                        text = secondaryActionLabel,
                        onClick = onSecondaryActionClick,
                        kind = PlusFisioButtonKind.Tertiary
                    )
                }
                if (tertiaryActionLabel != null && onTertiaryActionClick != null) {
                    PlusFisioButton(
                        text = tertiaryActionLabel,
                        onClick = onTertiaryActionClick,
                        kind = PlusFisioButtonKind.Tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun ClientItem(
    name: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    highlightText: String? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card
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
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PlusFisio.colors.brandSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (highlightText != null) {
                StatusChip(
                    text = highlightText,
                    tone = PlusFisioStatusTone.Warning
                )
            }
        }
    }
}

@Composable
fun PaymentItem(
    title: String,
    amount: String,
    subtitle: String,
    status: PaymentItemStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid1)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = amount,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                StatusChip(
                    text = paymentStatusLabel(status),
                    tone = paymentStatusTone(status)
                )
            }
        }
    }
}

@Composable
fun PackageSessionCard(
    packageName: String,
    statusTitle: String,
    supportingText: String,
    status: PackageSessionStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = PlusFisio.shapes.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
        ) {
            Text(
                text = packageName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = statusTitle,
                style = PlusFisio.typeScale.title24,
                color = packageTitleColor(status)
            )
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            StatusChip(
                text = packageStatusLabel(status),
                tone = packageStatusTone(status)
            )
        }
    }
}

@Composable
private fun summaryColors(tone: SummaryCardTone): SummaryColors = when (tone) {
    SummaryCardTone.Default -> SummaryColors(
        container = MaterialTheme.colorScheme.surface,
        primary = MaterialTheme.colorScheme.onSurface,
        secondary = MaterialTheme.colorScheme.onSurfaceVariant
    )

    SummaryCardTone.Highlight -> SummaryColors(
        container = PlusFisio.colors.brandSoft,
        primary = MaterialTheme.colorScheme.primary,
        secondary = MaterialTheme.colorScheme.onSurfaceVariant
    )

    SummaryCardTone.Alert -> SummaryColors(
        container = PlusFisio.colors.warning.copy(alpha = 0.16f),
        primary = PlusFisio.colors.warning,
        secondary = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private data class SummaryColors(
    val container: Color,
    val primary: Color,
    val secondary: Color
)

private fun appointmentStatusLabel(status: AppointmentItemStatus): String = when (status) {
    AppointmentItemStatus.Scheduled -> "Agendado"
    AppointmentItemStatus.Confirmed -> "Confirmado"
    AppointmentItemStatus.Attended -> "Atendido"
    AppointmentItemStatus.Missed -> "Falta"
}

private fun appointmentStatusTone(status: AppointmentItemStatus): PlusFisioStatusTone = when (status) {
    AppointmentItemStatus.Scheduled -> PlusFisioStatusTone.Neutral
    AppointmentItemStatus.Confirmed -> PlusFisioStatusTone.Info
    AppointmentItemStatus.Attended -> PlusFisioStatusTone.Success
    AppointmentItemStatus.Missed -> PlusFisioStatusTone.Error
}

private fun paymentStatusLabel(status: PaymentItemStatus): String = when (status) {
    PaymentItemStatus.Pending -> "Pendente"
    PaymentItemStatus.DueSoon -> "Vencendo"
    PaymentItemStatus.Paid -> "Pago"
}

private fun paymentStatusTone(status: PaymentItemStatus): PlusFisioStatusTone = when (status) {
    PaymentItemStatus.Pending -> PlusFisioStatusTone.Neutral
    PaymentItemStatus.DueSoon -> PlusFisioStatusTone.Warning
    PaymentItemStatus.Paid -> PlusFisioStatusTone.Success
}

private fun packageStatusLabel(status: PackageSessionStatus): String = when (status) {
    PackageSessionStatus.Active -> "Ativo"
    PackageSessionStatus.LowBalance -> "Baixo saldo"
    PackageSessionStatus.Expired -> "Expirado"
}

private fun packageStatusTone(status: PackageSessionStatus): PlusFisioStatusTone = when (status) {
    PackageSessionStatus.Active -> PlusFisioStatusTone.Success
    PackageSessionStatus.LowBalance -> PlusFisioStatusTone.Warning
    PackageSessionStatus.Expired -> PlusFisioStatusTone.Error
}

@Composable
private fun packageTitleColor(status: PackageSessionStatus): Color = when (status) {
    PackageSessionStatus.Active -> MaterialTheme.colorScheme.primary
    PackageSessionStatus.LowBalance -> PlusFisio.colors.warning
    PackageSessionStatus.Expired -> PlusFisio.colors.error
}

@Preview
@Composable
private fun PlusFisioOperationPreview() {
    PlusFisioTheme {
        Column(
            modifier = Modifier.padding(PlusFisio.spacing.grid4),
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
        ) {
            SectionHeader(title = "Confirmacoes pendentes", badgeText = "3")
            SummaryCard(
                title = "Vencimentos",
                value = "02",
                subtitle = "Resumo rapido do dia",
                tone = SummaryCardTone.Alert
            )
            AppointmentItem(
                timeLabel = "09:30",
                clientName = "Mariana Costa",
                details = "Pilates clinico - 50 min - Ana Souza",
                status = AppointmentItemStatus.Missed,
                primaryActionLabel = "Confirmar",
                onPrimaryActionClick = {},
                secondaryActionLabel = "WhatsApp",
                onSecondaryActionClick = {},
                tertiaryActionLabel = "Remarcar",
                onTertiaryActionClick = {}
            )
            ClientItem(
                name = "Juliana Martins",
                subtitle = "Pacote com 1 sessao restante",
                highlightText = "1"
            )
            PaymentItem(
                title = "Pacote Pilates 8 sessoes",
                amount = "R$ 320",
                subtitle = "Juliana Martins - vence 18 abr",
                status = PaymentItemStatus.Paid
            )
            PackageSessionCard(
                packageName = "Pacote Pilates 8 sessoes",
                statusTitle = "Pacote expirado",
                supportingText = "Validado ate 30 abr 2026",
                status = PackageSessionStatus.Expired
            )
        }
    }
}
