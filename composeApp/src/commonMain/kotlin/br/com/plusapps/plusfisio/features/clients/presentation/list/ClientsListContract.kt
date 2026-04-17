package br.com.plusapps.plusfisio.features.clients.presentation.list

import androidx.compose.runtime.Stable
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientFilterChipUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientListCardUi

@Stable
data class ClientsListState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val searchQuery: String = "",
    val summaryText: String = "",
    val sortHint: String = "",
    val filters: List<ClientFilterChipUi> = emptyList(),
    val clients: List<ClientListCardUi> = emptyList(),
    val emptyState: ClientsListEmptyState = ClientsListEmptyState.None
)

sealed interface ClientsListAction {
    data class OnSearchQueryChanged(val value: String) : ClientsListAction
    data class OnFilterSelected(val filter: ClientsListFilter) : ClientsListAction
    data class OnClientClicked(val clientId: String) : ClientsListAction
    data object OnCreateClientClicked : ClientsListAction
    data object OnRetryClicked : ClientsListAction
    data object OnHomeTabClicked : ClientsListAction
    data object OnAgendaTabClicked : ClientsListAction
    data object OnFinanceTabClicked : ClientsListAction
    data object OnMoreTabClicked : ClientsListAction
}

sealed interface ClientsListEvent {
    data class NavigateToClientDetail(val clientId: String) : ClientsListEvent
    data object NavigateToCreateClient : ClientsListEvent
    data object NavigateToHome : ClientsListEvent
    data class ShowMessage(val message: UiText) : ClientsListEvent
}

enum class ClientsListFilter(
    val tone: PlusFisioStatusTone
) {
    All(PlusFisioStatusTone.Info),
    Today(PlusFisioStatusTone.Info),
    Pending(PlusFisioStatusTone.Warning),
    Active(PlusFisioStatusTone.Success)
}

enum class ClientsListEmptyState {
    None,
    FirstClient,
    SearchResult,
    Error
}
