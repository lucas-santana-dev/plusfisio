package br.com.plusapps.plusfisio.features.clients.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.EmptyState
import br.com.plusapps.plusfisio.core.presentation.components.StatusChip
import br.com.plusapps.plusfisio.core.presentation.text.asString
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientInfoCard
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientQuickActionRow
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsBottomBar
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsShellTab
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsTopBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_detail_charge
import plusfisio.composeapp.generated.resources.clients_detail_edit
import plusfisio.composeapp.generated.resources.clients_detail_history
import plusfisio.composeapp.generated.resources.clients_detail_package
import plusfisio.composeapp.generated.resources.clients_detail_presence
import plusfisio.composeapp.generated.resources.clients_detail_schedule
import plusfisio.composeapp.generated.resources.clients_detail_summary
import plusfisio.composeapp.generated.resources.clients_detail_title
import plusfisio.composeapp.generated.resources.clients_detail_whatsapp
import plusfisio.composeapp.generated.resources.clients_error_action
import plusfisio.composeapp.generated.resources.clients_error_title
import plusfisio.composeapp.generated.resources.clients_package_card_title
import plusfisio.composeapp.generated.resources.clients_timeline_card_title

@Composable
fun ClientDetailRoot(
    session: AuthSession,
    clientId: String,
    refreshVersion: Int,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onNavigateToPackage: (String) -> Unit,
    onNavigateToHistory: (String) -> Unit,
    viewModel: ClientDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session.userId, clientId, refreshVersion) {
        viewModel.bind(session, clientId, refreshVersion)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ClientDetailEvent.NavigateBack -> onNavigateBack()
                ClientDetailEvent.NavigateHome -> onNavigateHome()
                is ClientDetailEvent.NavigateToEdit -> onNavigateToEdit(event.clientId)
                is ClientDetailEvent.NavigateToHistory -> onNavigateToHistory(event.clientId)
                is ClientDetailEvent.NavigateToPackage -> onNavigateToPackage(event.clientId)
                is ClientDetailEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier.padding(
                    start = PlusFisio.spacing.screenHorizontal,
                    end = PlusFisio.spacing.screenHorizontal,
                    bottom = PlusFisio.spacing.grid3
                )
            ) {
                ClientsBottomBar(
                    selectedTab = ClientsShellTab.Clients,
                    onTabClick = { tab ->
                        when (tab) {
                            ClientsShellTab.Home -> viewModel.onAction(ClientDetailAction.OnHomeTabClicked)
                            ClientsShellTab.Agenda -> viewModel.onAction(ClientDetailAction.OnAgendaTabClicked)
                            ClientsShellTab.Clients -> Unit
                            ClientsShellTab.Finance -> viewModel.onAction(ClientDetailAction.OnFinanceTabClicked)
                            ClientsShellTab.More -> viewModel.onAction(ClientDetailAction.OnMoreTabClicked)
                        }
                    }
                )
            }
        }
    ) { scaffoldPadding ->
        ClientDetailScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = scaffoldPadding
        )
    }
}

@Composable
fun ClientDetailScreen(
    state: ClientDetailState,
    onAction: (ClientDetailAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.errorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(PlusFisio.spacing.grid4)
        ) {
            EmptyState(
                title = stringResource(Res.string.clients_error_title),
                description = state.errorMessage.asString(),
                actionLabel = stringResource(Res.string.clients_error_action),
                onActionClick = { onAction(ClientDetailAction.OnRetryClicked) }
            )
        }
        return
    }

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = PlusFisio.spacing.screenHorizontal)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
    ) {
        ClientsTopBar(
            title = stringResource(Res.string.clients_detail_title),
            onBackClick = { onAction(ClientDetailAction.OnBackClicked) },
            actionLabel = stringResource(Res.string.clients_detail_edit),
            onActionClick = { onAction(ClientDetailAction.OnEditClicked) }
        )

        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
        ) {
            androidx.compose.material3.Text(
                text = state.name,
                style = PlusFisio.typeScale.title24,
                color = MaterialTheme.colorScheme.onSurface
            )
            androidx.compose.material3.Text(
                text = state.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            StatusChip(
                text = state.statusLabel,
                tone = state.statusTone
            )
        }

        ClientInfoCard(
            title = stringResource(Res.string.clients_detail_schedule),
            description = state.nextStepDescription
        )

        ClientQuickActionRow(
            primaryLabel = stringResource(Res.string.clients_detail_presence),
            primaryAction = { onAction(ClientDetailAction.OnPresenceClicked) },
            secondaryLabel = stringResource(Res.string.clients_detail_charge),
            secondaryAction = { onAction(ClientDetailAction.OnChargeClicked) }
        )

        ClientQuickActionRow(
            primaryLabel = stringResource(Res.string.clients_detail_whatsapp),
            primaryAction = { onAction(ClientDetailAction.OnWhatsappClicked) },
            secondaryLabel = stringResource(Res.string.clients_detail_package),
            secondaryAction = { onAction(ClientDetailAction.OnPackageClicked) }
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_detail_summary),
            description = state.summaryDescription
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_package_card_title),
            description = state.packageDescription,
            actionLabel = stringResource(Res.string.clients_detail_package),
            onActionClick = { onAction(ClientDetailAction.OnPackageClicked) }
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_timeline_card_title),
            description = state.historyDescription,
            actionLabel = stringResource(Res.string.clients_detail_history),
            onActionClick = { onAction(ClientDetailAction.OnHistoryClicked) }
        )
    }
}

@Preview
@Composable
private fun ClientDetailScreenPreview() {
    PlusFisioTheme {
        ClientDetailScreen(
            state = ClientDetailState(
                isLoading = false,
                clientId = "1",
                name = "Juliana Martins",
                subtitle = "Pilates solo • WhatsApp preferencial",
                statusLabel = "Ativo",
                nextStepDescription = "Sem agendamento futuro. Cliente ativo e pronto para novos retornos.",
                summaryDescription = "Cliente ativo, com contato pronto para agenda, cobranca simples e acompanhamentos futuros.",
                packageDescription = "Sem pacote ativo. O cliente pode seguir avulso ou receber um pacote na proxima etapa do fluxo.",
                historyDescription = "O historico ja registra cadastro, atualizacoes e campos preenchidos neste cliente."
            ),
            onAction = {}
        )
    }
}
