package br.com.plusapps.plusfisio.features.clients.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientFilterChipUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientHistoryItemCard
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientHistoryItemCardUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsBottomBar
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsFilterChip
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsShellTab
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsTopBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_error_action
import plusfisio.composeapp.generated.resources.clients_error_title
import plusfisio.composeapp.generated.resources.clients_history_title

@Composable
fun ClientHistoryRoot(
    session: AuthSession,
    clientId: String,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: ClientHistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session.userId, clientId) {
        viewModel.bind(session, clientId)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ClientHistoryEvent.NavigateBack -> onNavigateBack()
                ClientHistoryEvent.NavigateHome -> onNavigateHome()
                is ClientHistoryEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
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
                            ClientsShellTab.Home -> viewModel.onAction(ClientHistoryAction.OnHomeTabClicked)
                            ClientsShellTab.Agenda -> viewModel.onAction(ClientHistoryAction.OnAgendaTabClicked)
                            ClientsShellTab.Clients -> Unit
                            ClientsShellTab.Finance -> viewModel.onAction(ClientHistoryAction.OnFinanceTabClicked)
                            ClientsShellTab.More -> viewModel.onAction(ClientHistoryAction.OnMoreTabClicked)
                        }
                    }
                )
            }
        }
    ) { scaffoldPadding ->
        ClientHistoryScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = scaffoldPadding
        )
    }
}

@Composable
fun ClientHistoryScreen(
    state: ClientHistoryState,
    onAction: (ClientHistoryAction) -> Unit,
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
                onActionClick = { onAction(ClientHistoryAction.OnRetryClicked) }
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = PlusFisio.spacing.screenHorizontal),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
    ) {
        item {
            ClientsTopBar(
                title = stringResource(Res.string.clients_history_title),
                onBackClick = { onAction(ClientHistoryAction.OnBackClicked) }
            )
        }

        item {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
            ) {
                androidx.compose.material3.Text(
                    text = state.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                androidx.compose.foundation.layout.Row(
                    horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
                ) {
                    state.filters.forEachIndexed { index, chip ->
                        ClientsFilterChip(
                            chip = chip,
                            onClick = {
                                onAction(
                                    ClientHistoryAction.OnFilterSelected(
                                        ClientHistoryFilter.entries[index]
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        items(state.items) { item ->
            ClientHistoryItemCard(item = item)
        }
    }
}

@Preview
@Composable
private fun ClientHistoryScreenPreview() {
    PlusFisioTheme {
        ClientHistoryScreen(
            state = ClientHistoryState(
                isLoading = false,
                subtitle = "Jornada recente do cliente",
                filters = listOf(
                    ClientFilterChipUi("Todos", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info, true),
                    ClientFilterChipUi("Cadastro", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Success, false),
                    ClientFilterChipUi("Contato", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info, false)
                ),
                items = listOf(
                    ClientHistoryItemCardUi(
                        title = "Cadastro criado",
                        description = "Cliente cadastrado na base principal do estudio.",
                        timestampLabel = "Hoje",
                        tone = br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info
                    ),
                    ClientHistoryItemCardUi(
                        title = "Profissional responsavel",
                        description = "Ana Souza",
                        timestampLabel = "Ontem",
                        tone = br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Warning
                    )
                )
            ),
            onAction = {}
        )
    }
}
