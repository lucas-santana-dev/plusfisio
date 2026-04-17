package br.com.plusapps.plusfisio.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.ActionListSheet
import br.com.plusapps.plusfisio.core.presentation.components.EmptyState
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioBottomBar
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioBottomBarItem
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioButtonKind
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioFloatingActionButton
import br.com.plusapps.plusfisio.core.presentation.components.SheetAction
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.home.presentation.components.HomeAttentionBanner
import br.com.plusapps.plusfisio.features.home.presentation.components.HomeGreetingBlock
import br.com.plusapps.plusfisio.features.home.presentation.components.HomeHeader
import br.com.plusapps.plusfisio.features.home.presentation.components.HomeMetricCard
import br.com.plusapps.plusfisio.features.home.presentation.components.HomeSectionCard
import br.com.plusapps.plusfisio.features.home.presentation.components.NextAppointmentCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.home_agenda
import plusfisio.composeapp.generated.resources.home_clients
import plusfisio.composeapp.generated.resources.home_empty_card_action
import plusfisio.composeapp.generated.resources.home_empty_card_description
import plusfisio.composeapp.generated.resources.home_empty_card_title
import plusfisio.composeapp.generated.resources.home_fab
import plusfisio.composeapp.generated.resources.home_finance
import plusfisio.composeapp.generated.resources.home_home
import plusfisio.composeapp.generated.resources.home_loading
import plusfisio.composeapp.generated.resources.home_more
import plusfisio.composeapp.generated.resources.home_more_menu_description
import plusfisio.composeapp.generated.resources.home_more_menu_sign_out
import plusfisio.composeapp.generated.resources.home_more_menu_title
import plusfisio.composeapp.generated.resources.home_next_appointment_empty_action
import plusfisio.composeapp.generated.resources.home_next_appointment_empty_description
import plusfisio.composeapp.generated.resources.home_next_appointment_empty_secondary_action
import plusfisio.composeapp.generated.resources.home_next_appointment_empty_title
import plusfisio.composeapp.generated.resources.home_title
import plusfisio.composeapp.generated.resources.home_welcome_message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoot(
    session: AuthSession,
    onNavigateToClients: () -> Unit,
    onSignOutClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session.userId) {
        viewModel.bindSession(session)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                HomeEvent.NavigateToAgenda -> snackbarHostState.showSnackbar("Agenda em breve.")
                HomeEvent.NavigateToClients -> onNavigateToClients()
                HomeEvent.NavigateToFinance -> snackbarHostState.showSnackbar("Financeiro em breve.")
                HomeEvent.NavigateToNewAppointment -> snackbarHostState.showSnackbar("Novo agendamento em breve.")
                is HomeEvent.OpenWhatsApp -> snackbarHostState.showSnackbar("WhatsApp em breve.")
                HomeEvent.RequestSignOut -> onSignOutClick()
                is HomeEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    if (state.isMoreMenuVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onAction(HomeAction.OnDismissMoreMenu) },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            ActionListSheet(
                title = stringResource(Res.string.home_more_menu_title),
                description = stringResource(Res.string.home_more_menu_description),
                actions = listOf(
                    SheetAction(
                        label = stringResource(Res.string.home_more_menu_sign_out),
                        kind = PlusFisioButtonKind.Destructive,
                        onClick = { viewModel.onAction(HomeAction.OnSignOutClick) }
                    )
                ),
                modifier = Modifier.padding(
                    start = PlusFisio.spacing.screenHorizontal,
                    end = PlusFisio.spacing.screenHorizontal,
                    bottom = PlusFisio.spacing.grid5
                )
            )
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier.padding(
                    start = PlusFisio.spacing.screenHorizontal,
                    end = PlusFisio.spacing.screenHorizontal,
                    bottom = PlusFisio.spacing.grid3
                )
            ) {
                PlusFisioBottomBar {
                    HomeBottomBarItem(
                        label = stringResource(Res.string.home_home),
                        tab = HomeBottomTab.Home,
                        selectedTab = state.selectedBottomTab,
                        icon = Icons.Filled.Home,
                        onClick = viewModel::onAction
                    )
                    HomeBottomBarItem(
                        label = stringResource(Res.string.home_agenda),
                        tab = HomeBottomTab.Agenda,
                        selectedTab = state.selectedBottomTab,
                        icon = Icons.Filled.CalendarMonth,
                        onClick = viewModel::onAction
                    )
                    HomeBottomBarItem(
                        label = stringResource(Res.string.home_clients),
                        tab = HomeBottomTab.Clients,
                        selectedTab = state.selectedBottomTab,
                        icon = Icons.Filled.People,
                        onClick = viewModel::onAction
                    )
                    HomeBottomBarItem(
                        label = stringResource(Res.string.home_finance),
                        tab = HomeBottomTab.Finance,
                        selectedTab = state.selectedBottomTab,
                        icon = Icons.Filled.AccountBalanceWallet,
                        onClick = viewModel::onAction
                    )
                    HomeBottomBarItem(
                        label = stringResource(Res.string.home_more),
                        tab = HomeBottomTab.More,
                        selectedTab = state.selectedBottomTab,
                        icon = Icons.Filled.MoreHoriz,
                        onClick = viewModel::onAction
                    )
                }
            }
        },
        floatingActionButton = {
            PlusFisioFloatingActionButton(
                text = stringResource(Res.string.home_fab),
                onClick = { viewModel.onAction(HomeAction.OnNewAppointmentClick) }
            )
        }
    ) { scaffoldPadding ->
        HomeScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding,
            scaffoldPadding = scaffoldPadding
        )
    }
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val spacing = PlusFisio.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(scaffoldPadding)
            .padding(horizontal = spacing.screenHorizontal, vertical = spacing.screenVertical),
        verticalArrangement = Arrangement.spacedBy(spacing.contentGap)
    ) {
        HomeHeader(title = stringResource(Res.string.home_title))
        HomeGreetingBlock(
            message = stringResource(Res.string.home_welcome_message, state.professionalFirstName)
        )

        if (state.isLoading) {
            LoadingHomeCard()
        } else if (state.errorMessage != null) {
            EmptyState(
                title = stringResource(Res.string.home_empty_card_title),
                description = stringResource(Res.string.home_empty_card_description),
                actionLabel = stringResource(Res.string.home_empty_card_action),
                onActionClick = { onAction(HomeAction.OnRetryClick) }
            )
        } else {
            NextAppointmentCard(
                appointment = state.nextAppointment,
                emptyTitle = stringResource(Res.string.home_next_appointment_empty_title),
                emptyDescription = stringResource(Res.string.home_next_appointment_empty_description),
                primaryEmptyActionLabel = stringResource(Res.string.home_next_appointment_empty_action),
                emptySecondaryActionLabel = stringResource(Res.string.home_next_appointment_empty_secondary_action),
                onPrimaryClick = {
                    onAction(
                        if (state.nextAppointment != null) {
                            HomeAction.OnConfirmAppointmentClick
                        } else {
                            HomeAction.OnNewAppointmentClick
                        }
                    )
                },
                onSecondaryClick = {
                    onAction(
                        if (state.nextAppointment != null) {
                            HomeAction.OnWhatsappClick
                        } else {
                            HomeAction.OnAgendaSectionClick
                        }
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.grid4)
            ) {
                HomeMetricCard(
                    metric = state.metrics.appointments,
                    modifier = Modifier.weight(1f)
                )
                HomeMetricCard(
                    metric = state.metrics.pendings,
                    modifier = Modifier.weight(1f)
                )
            }

            state.attentionBanner?.let { banner ->
                HomeAttentionBanner(banner = banner)
            }

            HomeSectionCard(
                section = state.agendaSection,
                onClick = { onAction(HomeAction.OnAgendaSectionClick) }
            )

            HomeSectionCard(
                section = state.pendingConfirmationsSection,
                onClick = { onAction(HomeAction.OnPendingConfirmationsClick) }
            )
        }

        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun RowScope.HomeBottomBarItem(
    label: String,
    tab: HomeBottomTab,
    selectedTab: HomeBottomTab,
    icon: ImageVector,
    onClick: (HomeAction) -> Unit
) {
    PlusFisioBottomBarItem(
        label = label,
        icon = icon,
        selected = selectedTab == tab,
        onClick = { onClick(HomeAction.OnBottomTabClick(tab)) }
    )
}

@Composable
private fun LoadingHomeCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(PlusFisio.spacing.grid3))
        Text(
            text = stringResource(Res.string.home_loading),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun HomeScreenEmptyPreview() {
    PlusFisioTheme {
        HomeScreen(
            state = HomeState(
                professionalFirstName = "Ana",
                isLoading = false,
                metrics = HomeMetricsUi(
                    appointments = HomeMetricCardUi(
                        title = "Atendimentos",
                        value = "00",
                        subtitle = "Crie o primeiro do dia",
                        tone = HomeMetricCardTone.Default
                    ),
                    pendings = HomeMetricCardUi(
                        title = "Pendencias",
                        value = "00",
                        subtitle = "Nenhuma confirmacao",
                        tone = HomeMetricCardTone.Highlight
                    )
                ),
                agendaSection = HomeSectionUi(
                    title = "Agenda de hoje",
                    description = "Voce ainda nao tem atendimentos agendados. Comece criando o primeiro horario.",
                    badgeText = null,
                    isHighlighted = true
                ),
                pendingConfirmationsSection = HomeSectionUi(
                    title = "Confirmacoes pendentes",
                    description = "Nenhuma confirmacao pendente no momento.",
                    badgeText = null,
                    isHighlighted = false
                )
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenPopulatedPreview() {
    PlusFisioTheme {
        HomeScreen(
            state = HomeState(
                professionalFirstName = "Ana",
                isLoading = false,
                nextAppointment = HomeNextAppointmentUi(
                    chipLabel = "Proximo atendimento",
                    title = "09:30 - Mariana Costa",
                    details = "Pilates clinico - 50 min - pacote ativo",
                    primaryActionLabel = "Confirmar",
                    secondaryActionLabel = "WhatsApp",
                    whatsappPhone = "5511999999999"
                ),
                metrics = HomeMetricsUi(
                    appointments = HomeMetricCardUi(
                        title = "Atendimentos",
                        value = "08",
                        subtitle = "3 pela manha",
                        tone = HomeMetricCardTone.Default
                    ),
                    pendings = HomeMetricCardUi(
                        title = "Pendencias",
                        value = "03",
                        subtitle = "confirmacoes",
                        tone = HomeMetricCardTone.Highlight
                    )
                ),
                attentionBanner = HomeAttentionBannerUi(
                    message = "2 cobrancas e 1 pacote com poucas sessoes exigem atencao hoje."
                ),
                agendaSection = HomeSectionUi(
                    title = "Agenda de hoje",
                    description = "Bloco principal aberto por padrao com foco na sequencia imediata do dia.",
                    badgeText = "08",
                    isHighlighted = true
                ),
                pendingConfirmationsSection = HomeSectionUi(
                    title = "Confirmacoes pendentes",
                    description = "Secao recolhivel para evitar multiplas listas abertas ao mesmo tempo.",
                    badgeText = "03",
                    isHighlighted = false
                )
            ),
            onAction = {}
        )
    }
}
