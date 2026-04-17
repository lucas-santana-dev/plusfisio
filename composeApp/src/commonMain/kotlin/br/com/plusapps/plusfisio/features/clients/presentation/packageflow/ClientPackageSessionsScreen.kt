package br.com.plusapps.plusfisio.features.clients.presentation.packageflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import plusfisio.composeapp.generated.resources.clients_error_action
import plusfisio.composeapp.generated.resources.clients_error_title
import plusfisio.composeapp.generated.resources.clients_package_adjust
import plusfisio.composeapp.generated.resources.clients_package_current_title
import plusfisio.composeapp.generated.resources.clients_package_next_step
import plusfisio.composeapp.generated.resources.clients_package_register
import plusfisio.composeapp.generated.resources.clients_package_register_missed
import plusfisio.composeapp.generated.resources.clients_package_renew
import plusfisio.composeapp.generated.resources.clients_package_title
import plusfisio.composeapp.generated.resources.clients_package_usage_title
import plusfisio.composeapp.generated.resources.clients_package_history_title

@Composable
fun ClientPackageSessionsRoot(
    session: AuthSession,
    clientId: String,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: ClientPackageSessionsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session.userId, clientId) {
        viewModel.bind(session, clientId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ClientPackageSessionsEvent.NavigateBack -> onNavigateBack()
                ClientPackageSessionsEvent.NavigateHome -> onNavigateHome()
                is ClientPackageSessionsEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
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
                            ClientsShellTab.Home -> viewModel.onAction(ClientPackageSessionsAction.OnHomeTabClicked)
                            ClientsShellTab.Agenda -> viewModel.onAction(ClientPackageSessionsAction.OnAgendaTabClicked)
                            ClientsShellTab.Clients -> Unit
                            ClientsShellTab.Finance -> viewModel.onAction(ClientPackageSessionsAction.OnFinanceTabClicked)
                            ClientsShellTab.More -> viewModel.onAction(ClientPackageSessionsAction.OnMoreTabClicked)
                        }
                    }
                )
            }
        }
    ) { scaffoldPadding ->
        ClientPackageSessionsScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = scaffoldPadding
        )
    }
}

@Composable
fun ClientPackageSessionsScreen(
    state: ClientPackageSessionsState,
    onAction: (ClientPackageSessionsAction) -> Unit,
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
                onActionClick = { onAction(ClientPackageSessionsAction.OnRetryClicked) }
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
            title = stringResource(Res.string.clients_package_title),
            onBackClick = { onAction(ClientPackageSessionsAction.OnBackClicked) }
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_package_current_title),
            description = "${state.packageTitle}\n${state.packageDescription}"
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_package_usage_title),
            description = state.usageSummary
        )

        ClientQuickActionRow(
            primaryLabel = stringResource(Res.string.clients_package_register),
            primaryAction = { onAction(ClientPackageSessionsAction.OnRegisterAttendanceClicked) },
            secondaryLabel = stringResource(Res.string.clients_package_register_missed),
            secondaryAction = { onAction(ClientPackageSessionsAction.OnRegisterMissedClicked) }
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_package_next_step),
            description = state.nextRecommendation
        )

        ClientQuickActionRow(
            primaryLabel = stringResource(Res.string.clients_package_renew),
            primaryAction = { onAction(ClientPackageSessionsAction.OnRenewPackageClicked) },
            secondaryLabel = stringResource(Res.string.clients_package_adjust),
            secondaryAction = { onAction(ClientPackageSessionsAction.OnAdjustBalanceClicked) }
        )

        ClientInfoCard(
            title = stringResource(Res.string.clients_package_history_title),
            description = state.historyItems.joinToString(separator = "\n• ", prefix = "• ")
        )
    }
}

@Preview
@Composable
private fun ClientPackageSessionsPreview() {
    PlusFisioTheme {
        ClientPackageSessionsScreen(
            state = ClientPackageSessionsState(
                isLoading = false,
                packageTitle = "Sem pacote ativo",
                packageDescription = "Cliente pronto para vincular um pacote assim que o fluxo de sessoes entrar.",
                usageSummary = "Ainda nao ha consumo de sessoes registrado para este cliente.",
                nextRecommendation = "Definir o profissional responsavel ajuda a fechar a operacao antes de criar o pacote.",
                historyItems = listOf(
                    "Cadastro do cliente pronto para receber pacote.",
                    "Modalidade principal definida.",
                    "Observacoes ajudam a orientar proximos passos."
                )
            ),
            onAction = {}
        )
    }
}
