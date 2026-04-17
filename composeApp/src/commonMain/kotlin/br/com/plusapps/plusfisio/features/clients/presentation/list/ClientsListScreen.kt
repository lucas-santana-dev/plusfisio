package br.com.plusapps.plusfisio.features.clients.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.plusapps.plusfisio.core.presentation.components.EmptyState
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioFloatingActionButton
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioSearchField
import br.com.plusapps.plusfisio.core.presentation.text.resolve
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisio
import br.com.plusapps.plusfisio.core.presentation.theme.PlusFisioTheme
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientFilterChipUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientListCardUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientListCard
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsBottomBar
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsFilterChip
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsShellTab
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientsTopBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_empty_action
import plusfisio.composeapp.generated.resources.clients_empty_description
import plusfisio.composeapp.generated.resources.clients_empty_title
import plusfisio.composeapp.generated.resources.clients_error_action
import plusfisio.composeapp.generated.resources.clients_error_description
import plusfisio.composeapp.generated.resources.clients_error_title
import plusfisio.composeapp.generated.resources.clients_fab
import plusfisio.composeapp.generated.resources.clients_list_label
import plusfisio.composeapp.generated.resources.clients_search_empty_action
import plusfisio.composeapp.generated.resources.clients_search_empty_description
import plusfisio.composeapp.generated.resources.clients_search_empty_title
import plusfisio.composeapp.generated.resources.clients_search_hint

@Composable
fun ClientsListRoot(
    session: AuthSession,
    refreshVersion: Int,
    onNavigateHome: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToClientDetail: (String) -> Unit,
    viewModel: ClientsListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(session.userId, refreshVersion) {
        viewModel.bindSession(session, refreshVersion)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ClientsListEvent.NavigateToClientDetail -> onNavigateToClientDetail(event.clientId)
                ClientsListEvent.NavigateToCreateClient -> onNavigateToCreate()
                ClientsListEvent.NavigateToHome -> onNavigateHome()
                is ClientsListEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message.resolve())
            }
        }
    }

    Scaffold(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                            ClientsShellTab.Home -> viewModel.onAction(ClientsListAction.OnHomeTabClicked)
                            ClientsShellTab.Agenda -> viewModel.onAction(ClientsListAction.OnAgendaTabClicked)
                            ClientsShellTab.Clients -> Unit
                            ClientsShellTab.Finance -> viewModel.onAction(ClientsListAction.OnFinanceTabClicked)
                            ClientsShellTab.More -> viewModel.onAction(ClientsListAction.OnMoreTabClicked)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            PlusFisioFloatingActionButton(
                text = stringResource(Res.string.clients_fab),
                onClick = { viewModel.onAction(ClientsListAction.OnCreateClientClicked) }
            )
        }
    ) { scaffoldPadding ->
        ClientsListScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = scaffoldPadding
        )
    }
}

@Composable
fun ClientsListScreen(
    state: ClientsListState,
    onAction: (ClientsListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = PlusFisio.spacing.screenHorizontal),
        contentPadding = PaddingValues(
            top = PlusFisio.spacing.grid4,
            bottom = 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid4)
    ) {
        item {
            ClientsTopBar(
                title = stringResource(Res.string.clients_list_label)
            )
        }

        item {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid3)
            ) {
                androidx.compose.material3.Text(
                    text = state.summaryText,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
                PlusFisioSearchField(
                    value = state.searchQuery,
                    onValueChange = { onAction(ClientsListAction.OnSearchQueryChanged(it)) },
                    label = stringResource(Res.string.clients_list_label),
                    placeholder = stringResource(Res.string.clients_search_hint),
                    modifier = Modifier.fillMaxWidth()
                )
                androidx.compose.foundation.layout.Row(
                    horizontalArrangement = Arrangement.spacedBy(PlusFisio.spacing.grid2)
                ) {
                    state.filters.forEachIndexed { index, chip ->
                        ClientsFilterChip(
                            chip = chip,
                            onClick = {
                                onAction(
                                    ClientsListAction.OnFilterSelected(
                                        ClientsListFilter.entries[index]
                                    )
                                )
                            }
                        )
                    }
                }
                androidx.compose.material3.Text(
                    text = state.sortHint,
                    style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        when {
            state.isLoading -> {
                item {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }

            state.emptyState == ClientsListEmptyState.Error -> {
                item {
                    EmptyState(
                        title = stringResource(Res.string.clients_error_title),
                        description = stringResource(Res.string.clients_error_description),
                        actionLabel = stringResource(Res.string.clients_error_action),
                        onActionClick = { onAction(ClientsListAction.OnRetryClicked) }
                    )
                }
            }

            state.emptyState == ClientsListEmptyState.FirstClient -> {
                item {
                    EmptyState(
                        title = stringResource(Res.string.clients_empty_title),
                        description = stringResource(Res.string.clients_empty_description),
                        actionLabel = stringResource(Res.string.clients_empty_action),
                        onActionClick = { onAction(ClientsListAction.OnCreateClientClicked) }
                    )
                }
            }

            state.emptyState == ClientsListEmptyState.SearchResult -> {
                item {
                    EmptyState(
                        title = stringResource(Res.string.clients_search_empty_title),
                        description = stringResource(Res.string.clients_search_empty_description),
                        actionLabel = stringResource(Res.string.clients_search_empty_action),
                        onActionClick = {
                            onAction(ClientsListAction.OnSearchQueryChanged(""))
                            onAction(ClientsListAction.OnFilterSelected(ClientsListFilter.All))
                        }
                    )
                }
            }

            else -> {
                items(
                    items = state.clients,
                    key = ClientListCardUi::clientId
                ) { client ->
                    ClientListCard(
                        ui = client,
                        onClick = {
                            onAction(ClientsListAction.OnClientClicked(client.clientId))
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ClientsListScreenPreview() {
    PlusFisioTheme {
        ClientsListScreen(
            state = ClientsListState(
                isLoading = false,
                summaryText = "124 clientes • 18 com atencao",
                sortHint = "Ordenar: pendencias primeiro",
                filters = listOf(
                    ClientFilterChipUi("Todos", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info, true),
                    ClientFilterChipUi("Hoje", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info, false),
                    ClientFilterChipUi("Pendencias", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Warning, false),
                    ClientFilterChipUi("Ativos", br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Success, false)
                ),
                clients = listOf(
                    ClientListCardUi(
                        clientId = "1",
                        initials = "JM",
                        name = "Juliana Martins",
                        subtitle = "Cadastro ainda pede atencao em alguns campos importantes.",
                        badgeText = "Pendencias",
                        badgeTone = br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Warning
                    ),
                    ClientListCardUi(
                        clientId = "2",
                        initials = "MC",
                        name = "Mariana Costa",
                        subtitle = "Pilates solo • WhatsApp preferencial",
                        badgeText = "Hoje",
                        badgeTone = br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info
                    )
                )
            ),
            onAction = {}
        )
    }
}
