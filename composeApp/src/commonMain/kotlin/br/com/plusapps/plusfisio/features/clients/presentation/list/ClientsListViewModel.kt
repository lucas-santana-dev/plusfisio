package br.com.plusapps.plusfisio.features.clients.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.currentEpochMillis
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.domain.model.Client
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.domain.ClientRepository
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientFilterChipUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientListCardUi
import br.com.plusapps.plusfisio.features.clients.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_feature_pending

class ClientsListViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        ClientsListState(
            sortHint = ""
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<ClientsListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var boundStudioId: String? = null
    private var boundRefreshVersion: Int? = null
    private var allClients: List<Client> = emptyList()
    private var selectedFilter: ClientsListFilter = ClientsListFilter.All

    fun bindSession(session: AuthSession, refreshVersion: Int) {
        val studioId = session.studioId ?: return
        if (boundStudioId == studioId && boundRefreshVersion == refreshVersion) return

        boundStudioId = studioId
        boundRefreshVersion = refreshVersion
        loadClients(studioId)
    }

    fun onAction(action: ClientsListAction) {
        when (action) {
            ClientsListAction.OnAgendaTabClicked,
            ClientsListAction.OnFinanceTabClicked,
            ClientsListAction.OnMoreTabClicked -> emitFeaturePendingMessage()

            is ClientsListAction.OnClientClicked -> {
                emitEvent(ClientsListEvent.NavigateToClientDetail(action.clientId))
            }

            ClientsListAction.OnCreateClientClicked -> emitEvent(ClientsListEvent.NavigateToCreateClient)
            ClientsListAction.OnHomeTabClicked -> emitEvent(ClientsListEvent.NavigateToHome)
            is ClientsListAction.OnFilterSelected -> {
                selectedFilter = action.filter
                renderClients()
            }

            is ClientsListAction.OnSearchQueryChanged -> {
                _state.update {
                    it.copy(
                        searchQuery = action.value,
                        errorMessage = null
                    )
                }
                renderClients()
            }

            ClientsListAction.OnRetryClicked -> {
                boundStudioId?.let(::loadClients)
            }
        }
    }

    private fun loadClients(studioId: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    emptyState = ClientsListEmptyState.None
                )
            }

            clientRepository.getClients(studioId)
                .onSuccess { clients ->
                    allClients = clients
                    renderClients()
                }
                .onFailure { error ->
                    allClients = emptyList()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUiText(),
                            clients = emptyList(),
                            filters = buildFilters(selectedFilter),
                            summaryText = "0 clientes",
                            sortHint = "Ordenar: pendencias primeiro",
                            emptyState = ClientsListEmptyState.Error
                        )
                    }
                }
        }
    }

    private fun renderClients() {
        val now = currentEpochMillis()
        val visibleClients = allClients
            .filter { client -> client.matches(selectedFilter, now) }
            .filter { client -> client.matchesSearch(_state.value.searchQuery) }
            .sortedWith(
                compareByDescending<Client> { it.needsAttention() }
                    .thenByDescending { it.wasUpdatedToday(now) }
                    .thenBy { it.fullName.lowercase() }
            )

        val attentionCount = allClients.count { it.needsAttention() }
        val summaryText = when {
            allClients.isEmpty() -> "0 clientes"
            attentionCount > 0 -> "${allClients.size} clientes • $attentionCount com atencao"
            else -> "${allClients.size} clientes"
        }

        _state.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                summaryText = summaryText,
                sortHint = "Ordenar: pendencias primeiro",
                filters = buildFilters(selectedFilter),
                clients = visibleClients.map { client -> client.toListCardUi(now) },
                emptyState = resolveEmptyState(
                    hasAnyClients = allClients.isNotEmpty(),
                    hasVisibleClients = visibleClients.isNotEmpty(),
                    query = it.searchQuery
                )
            )
        }
    }

    private fun resolveEmptyState(
        hasAnyClients: Boolean,
        hasVisibleClients: Boolean,
        query: String
    ): ClientsListEmptyState {
        return when {
            hasVisibleClients -> ClientsListEmptyState.None
            hasAnyClients -> ClientsListEmptyState.SearchResult
            query.isNotBlank() -> ClientsListEmptyState.SearchResult
            else -> ClientsListEmptyState.FirstClient
        }
    }

    private fun buildFilters(selected: ClientsListFilter): List<ClientFilterChipUi> {
        return listOf(
            ClientsListFilter.All,
            ClientsListFilter.Today,
            ClientsListFilter.Pending,
            ClientsListFilter.Active
        ).map { filter ->
            ClientFilterChipUi(
                label = filter.toLabel(),
                tone = filter.tone,
                isSelected = filter == selected
            )
        }
    }

    private fun emitFeaturePendingMessage() {
        emitEvent(
            ClientsListEvent.ShowMessage(
                UiText.Resource(Res.string.clients_feature_pending)
            )
        )
    }

    private fun emitEvent(event: ClientsListEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

private fun ClientsListFilter.toLabel(): String {
    return when (this) {
        ClientsListFilter.All -> "Todos"
        ClientsListFilter.Today -> "Hoje"
        ClientsListFilter.Pending -> "Pendencias"
        ClientsListFilter.Active -> "Ativos"
    }
}

private fun Client.matchesSearch(query: String): Boolean {
    val normalizedQuery = query.trim().lowercase()
    if (normalizedQuery.isBlank()) return true

    val compactQuery = normalizedQuery.filterNot(Char::isWhitespace)
    val nameMatches = searchName.contains(normalizedQuery)
    val phoneMatches = phone.contains(compactQuery) || whatsappPhone.orEmpty().contains(compactQuery)

    return nameMatches || phoneMatches
}

private fun Client.matches(filter: ClientsListFilter, now: Long): Boolean {
    return when (filter) {
        ClientsListFilter.All -> true
        ClientsListFilter.Today -> wasUpdatedToday(now)
        ClientsListFilter.Pending -> needsAttention()
        ClientsListFilter.Active -> status.equals("active", ignoreCase = true)
    }
}

private fun Client.needsAttention(): Boolean {
    return email.isNullOrBlank() ||
        primaryModality.isNullOrBlank() ||
        responsibleProfessional.isNullOrBlank() ||
        acquisitionSource.isNullOrBlank()
}

private fun Client.wasUpdatedToday(now: Long): Boolean {
    val millisecondsPerDay = 24L * 60L * 60L * 1000L
    return updatedAtEpochMillis / millisecondsPerDay == now / millisecondsPerDay
}

private fun Client.toListCardUi(now: Long): ClientListCardUi {
    val badgeText = when {
        needsAttention() -> "Pendencias"
        wasUpdatedToday(now) -> "Hoje"
        status.equals("active", ignoreCase = true) -> "Ativo"
        else -> null
    }

    val badgeTone = when {
        needsAttention() -> br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Warning
        wasUpdatedToday(now) -> br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Info
        else -> br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone.Success
    }

    val subtitle = when {
        needsAttention() -> "Cadastro ainda pede atencao em alguns campos importantes."
        wasUpdatedToday(now) -> "Cadastro atualizado hoje e pronto para novos retornos."
        !primaryModality.isNullOrBlank() && !whatsappPhone.isNullOrBlank() ->
            "$primaryModality • WhatsApp preferencial"
        !primaryModality.isNullOrBlank() -> primaryModality
        else -> "Cliente ativo, pronto para agenda e cobranca simples."
    }

    return ClientListCardUi(
        clientId = clientId,
        initials = fullName.toInitials(),
        name = fullName,
        subtitle = subtitle,
        badgeText = badgeText,
        badgeTone = badgeTone
    )
}

private fun String.toInitials(): String {
    val words = trim()
        .split(" ")
        .filter { it.isNotBlank() }

    return when {
        words.isEmpty() -> "CL"
        words.size == 1 -> words.first().take(2).uppercase()
        else -> "${words.first().first()}${words.last().first()}".uppercase()
    }
}
