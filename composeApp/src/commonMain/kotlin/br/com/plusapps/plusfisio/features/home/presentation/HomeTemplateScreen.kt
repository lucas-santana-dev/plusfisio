package br.com.plusapps.plusfisio.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.plusapps.plusfisio.core.presentation.components.AppointmentItem
import br.com.plusapps.plusfisio.core.presentation.components.AppointmentItemStatus
import br.com.plusapps.plusfisio.core.presentation.components.ClientItem
import br.com.plusapps.plusfisio.core.presentation.components.EmptyState
import br.com.plusapps.plusfisio.core.presentation.components.PackageSessionCard
import br.com.plusapps.plusfisio.core.presentation.components.PackageSessionStatus
import br.com.plusapps.plusfisio.core.presentation.components.PaymentItem
import br.com.plusapps.plusfisio.core.presentation.components.PaymentItemStatus
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioBottomBar
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioBottomBarItem
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioFloatingActionButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioTopAppBar
import br.com.plusapps.plusfisio.core.presentation.components.SectionHeader
import br.com.plusapps.plusfisio.core.presentation.components.SummaryCard
import br.com.plusapps.plusfisio.core.presentation.components.SummaryCardTone
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.auth.presentation.components.AuthBackground
import br.com.plusapps.plusfisio.core.domain.model.StudioUserRole
import org.jetbrains.compose.resources.stringResource
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.home_template_appointment_confirm
import plusfisio.composeapp.generated.resources.home_template_appointment_details
import plusfisio.composeapp.generated.resources.home_template_appointment_reschedule
import plusfisio.composeapp.generated.resources.home_template_appointment_title_client
import plusfisio.composeapp.generated.resources.home_template_appointment_title_time
import plusfisio.composeapp.generated.resources.home_template_appointment_whatsapp
import plusfisio.composeapp.generated.resources.home_template_client_name
import plusfisio.composeapp.generated.resources.home_template_client_subtitle
import plusfisio.composeapp.generated.resources.home_template_dashboard
import plusfisio.composeapp.generated.resources.home_template_empty_description
import plusfisio.composeapp.generated.resources.home_template_empty_title
import plusfisio.composeapp.generated.resources.home_template_finance
import plusfisio.composeapp.generated.resources.home_template_greeting
import plusfisio.composeapp.generated.resources.home_template_nav_agenda
import plusfisio.composeapp.generated.resources.home_template_nav_clients
import plusfisio.composeapp.generated.resources.home_template_nav_finance
import plusfisio.composeapp.generated.resources.home_template_nav_home
import plusfisio.composeapp.generated.resources.home_template_nav_more
import plusfisio.composeapp.generated.resources.home_template_new_appointment
import plusfisio.composeapp.generated.resources.home_template_package_subtitle
import plusfisio.composeapp.generated.resources.home_template_package_title
import plusfisio.composeapp.generated.resources.home_template_payment_amount
import plusfisio.composeapp.generated.resources.home_template_payment_subtitle
import plusfisio.composeapp.generated.resources.home_template_payment_title
import plusfisio.composeapp.generated.resources.home_template_search
import plusfisio.composeapp.generated.resources.home_template_sign_out
import plusfisio.composeapp.generated.resources.home_template_summary_confirmation_subtitle
import plusfisio.composeapp.generated.resources.home_template_summary_confirmation_title
import plusfisio.composeapp.generated.resources.home_template_summary_confirmation_value
import plusfisio.composeapp.generated.resources.home_template_summary_due_subtitle
import plusfisio.composeapp.generated.resources.home_template_summary_due_title
import plusfisio.composeapp.generated.resources.home_template_summary_due_value
import plusfisio.composeapp.generated.resources.home_template_title

@Composable
fun HomeTemplateScreen(
    session: AuthSession,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val spacing = PlusFisio.spacing

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            Box(
                modifier = Modifier.padding(
                    start = spacing.screenHorizontal,
                    end = spacing.screenHorizontal,
                    bottom = spacing.grid3
                )
            ) {
                PlusFisioBottomBar {
                    PlusFisioBottomBarItem(
                        label = stringResource(Res.string.home_template_nav_home),
                        icon = Icons.Filled.Home,
                        selected = true,
                        onClick = {}
                    )
                    PlusFisioBottomBarItem(
                        label = stringResource(Res.string.home_template_nav_agenda),
                        icon = Icons.Filled.CalendarMonth,
                        selected = false,
                        onClick = {}
                    )
                    PlusFisioBottomBarItem(
                        label = stringResource(Res.string.home_template_nav_clients),
                        icon = Icons.Filled.People,
                        selected = false,
                        onClick = {}
                    )
                    PlusFisioBottomBarItem(
                        label = stringResource(Res.string.home_template_nav_finance),
                        icon = Icons.Filled.AccountBalanceWallet,
                        selected = false,
                        onClick = {}
                    )
                    PlusFisioBottomBarItem(
                        label = stringResource(Res.string.home_template_nav_more),
                        icon = Icons.Filled.MoreHoriz,
                        selected = false,
                        onClick = {},
                        badgeText = "3"
                    )
                }
            }
        },
        floatingActionButton = {
            PlusFisioFloatingActionButton(
                text = stringResource(Res.string.home_template_new_appointment),
                onClick = {}
            )
        }
    ) { scaffoldPadding ->
        AuthBackground(contentPadding = contentPadding) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(scaffoldPadding)
                    .padding(horizontal = spacing.screenHorizontal, vertical = spacing.screenVertical),
                verticalArrangement = Arrangement.spacedBy(spacing.grid4)
            ) {
                Text(
                    text = stringResource(
                        Res.string.home_template_greeting,
                        session.displayName ?: session.email
                    ),
                    style = PlusFisio.typeScale.body16,
                    color = PlusFisio.colors.brand
                )
                PlusFisioTopAppBar(
                    title = stringResource(Res.string.home_template_title),
                    actionLabel = stringResource(Res.string.home_template_sign_out),
                    onActionClick = onSignOutClick
                )
                Text(
                    text = stringResource(Res.string.home_template_dashboard),
                    style = PlusFisio.typeScale.body16,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.grid3)
                ) {
                    SectionHeader(
                        title = stringResource(Res.string.home_template_summary_confirmation_title),
                        badgeText = stringResource(Res.string.home_template_summary_confirmation_value)
                    )
                    SummaryCard(
                        title = stringResource(Res.string.home_template_summary_due_title),
                        value = stringResource(Res.string.home_template_summary_due_value),
                        subtitle = stringResource(Res.string.home_template_summary_due_subtitle),
                        tone = SummaryCardTone.Alert,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SummaryCard(
                        title = stringResource(Res.string.home_template_summary_confirmation_title),
                        value = stringResource(Res.string.home_template_summary_confirmation_value),
                        subtitle = stringResource(Res.string.home_template_summary_confirmation_subtitle),
                        tone = SummaryCardTone.Highlight,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SectionHeader(
                    title = stringResource(Res.string.home_template_finance),
                    badgeText = stringResource(Res.string.home_template_search)
                )
                AppointmentItem(
                    timeLabel = stringResource(Res.string.home_template_appointment_title_time),
                    clientName = stringResource(Res.string.home_template_appointment_title_client),
                    details = stringResource(Res.string.home_template_appointment_details),
                    status = AppointmentItemStatus.Missed,
                    primaryActionLabel = stringResource(Res.string.home_template_appointment_confirm),
                    onPrimaryActionClick = {},
                    secondaryActionLabel = stringResource(Res.string.home_template_appointment_whatsapp),
                    onSecondaryActionClick = {},
                    tertiaryActionLabel = stringResource(Res.string.home_template_appointment_reschedule),
                    onTertiaryActionClick = {}
                )
                ClientItem(
                    name = stringResource(Res.string.home_template_client_name),
                    subtitle = stringResource(Res.string.home_template_client_subtitle),
                    highlightText = "1"
                )
                PaymentItem(
                    title = stringResource(Res.string.home_template_payment_title),
                    amount = stringResource(Res.string.home_template_payment_amount),
                    subtitle = stringResource(Res.string.home_template_payment_subtitle),
                    status = PaymentItemStatus.Paid
                )
                PackageSessionCard(
                    packageName = stringResource(Res.string.home_template_payment_title),
                    statusTitle = stringResource(Res.string.home_template_package_title),
                    supportingText = stringResource(Res.string.home_template_package_subtitle),
                    status = PackageSessionStatus.Expired
                )
                EmptyState(
                    title = stringResource(Res.string.home_template_empty_title),
                    description = stringResource(Res.string.home_template_empty_description),
                    actionLabel = stringResource(Res.string.home_template_new_appointment),
                    onActionClick = {}
                )
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Preview
@Composable
private fun HomeTemplateScreenPreview() {
    PlusFisioTheme {
        HomeTemplateScreen(
            session = AuthSession(
                userId = "1",
                email = "owner@plusfisio.com",
                displayName = "Camila Rocha",
                studioId = "studio-demo",
                role = StudioUserRole.OwnerAdmin
            ),
            onSignOutClick = {}
        )
    }
}
