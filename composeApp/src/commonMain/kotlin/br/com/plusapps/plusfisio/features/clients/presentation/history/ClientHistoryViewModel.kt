package br.com.plusapps.plusfisio.features.clients.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.currentEpochMillis
import br.com.plusapps.plusfisio.core.domain.model.Client
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.domain.ClientRepository
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientFilterChipUi
import br.com.plusapps.plusfisio.features.clients.presentation.components.ClientHistoryItemCardUi
import br.com.plusapps.plusfisio.features.clients.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_feature_pending

class ClientHistoryViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClientHistoryState())
    val state = _state.asStateFlow()

    private val _events = Channel<ClientHistoryEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var allItems: List<HistoryEntry> = emptyList()
    private var selectedFilter: ClientHistoryFilter = ClientHistoryFilter.All
    private var boundStudioId: String? = null
    private var boundClientId: String? = null

    fun bind(session: AuthSession, clientId: String) {
        val studioId = session.studioId ?: return
        if (boundStudioId == studioId && boundClientId == clientId) return

        boundStudioId = studioId
        boundClientId = clientId
        loadClient(studioId, clientId)
    }

    fun onAction(action: ClientHistoryAction) {
        when (action) {
            ClientHistoryAction.OnAgendaTabClicked,
            ClientHistoryAction.OnFinanceTabClicked,
            ClientHistoryAction.OnMoreTabClicked -> emitFeaturePendingMessage()
            ClientHistoryAction.OnBackClicked -> emitEvent(ClientHistoryEvent.NavigateBack)
            is ClientHistoryAction.OnFilterSelected -> {
                selectedFilter = action.filter
                renderItems()
            }
            ClientHistoryAction.OnHomeTabClicked -> emitEvent(ClientHistoryEvent.NavigateHome)
            ClientHistoryAction.OnRetryClicked -> {
                val studioId = boundStudioId ?: return
                val clientId = boundClientId ?: return
                loadClient(studioId, clientId)
            }
        }
    }

    private fun loadClient(studioId: String, clientId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            clientRepository.getClient(studioId, clientId)
                .onSuccess { client ->
                    allItems = client.toHistoryEntries()
                    renderItems()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun renderItems() {
        val visibleItems = allItems
            .filter { entry -> selectedFilter == ClientHistoryFilter.All || entry.filter == selectedFilter }
            .map { entry -> entry.toCardUi() }

        _state.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                subtitle = "Jornada recente do cliente",
                filters = buildFilters(selectedFilter),
                items = visibleItems
            )
        }
    }

    private fun buildFilters(selected: ClientHistoryFilter): List<ClientFilterChipUi> {
        return ClientHistoryFilter.entries.map { filter ->
            ClientFilterChipUi(
                label = filter.toLabel(),
                tone = filter.tone,
                isSelected = selected == filter
            )
        }
    }

    private fun emitFeaturePendingMessage() {
        emitEvent(
            ClientHistoryEvent.ShowMessage(
                UiText.Resource(Res.string.clients_feature_pending)
            )
        )
    }

    private fun emitEvent(event: ClientHistoryEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

private data class HistoryEntry(
    val title: String,
    val description: String,
    val timestamp: Long,
    val timestampLabel: String,
    val tone: PlusFisioStatusTone,
    val filter: ClientHistoryFilter
)

private fun HistoryEntry.toCardUi(): ClientHistoryItemCardUi {
    return ClientHistoryItemCardUi(
        title = title,
        description = description,
        timestampLabel = timestampLabel,
        tone = tone
    )
}

private fun Client.toHistoryEntries(): List<HistoryEntry> {
    val now = currentEpochMillis()

    return buildList {
        add(
            HistoryEntry(
                title = "Cadastro criado",
                description = "Cliente cadastrado na base principal do estudio.",
                timestamp = createdAtEpochMillis,
                timestampLabel = createdAtEpochMillis.toRelativeLabel(now),
                tone = PlusFisioStatusTone.Info,
                filter = ClientHistoryFilter.Registration
            )
        )

        if (updatedAtEpochMillis > createdAtEpochMillis) {
            add(
                HistoryEntry(
                    title = "Cadastro atualizado",
                    description = "Dados principais revisados para manter a operacao consistente.",
                    timestamp = updatedAtEpochMillis,
                    timestampLabel = updatedAtEpochMillis.toRelativeLabel(now),
                    tone = PlusFisioStatusTone.Success,
                    filter = ClientHistoryFilter.Registration
                )
            )
        }

        if (!email.isNullOrBlank()) {
            add(
                HistoryEntry(
                    title = "Contato por e-mail",
                    description = "Endereco de e-mail registrado para futuras cobrancas e mensagens.",
                    timestamp = updatedAtEpochMillis,
                    timestampLabel = updatedAtEpochMillis.toRelativeLabel(now),
                    tone = PlusFisioStatusTone.Info,
                    filter = ClientHistoryFilter.Contact
                )
            )
        }

        if (!primaryModality.isNullOrBlank()) {
            add(
                HistoryEntry(
                    title = "Modalidade principal",
                    description = primaryModality,
                    timestamp = updatedAtEpochMillis,
                    timestampLabel = updatedAtEpochMillis.toRelativeLabel(now),
                    tone = PlusFisioStatusTone.Warning,
                    filter = ClientHistoryFilter.Profile
                )
            )
        }

        if (!responsibleProfessional.isNullOrBlank()) {
            add(
                HistoryEntry(
                    title = "Profissional responsavel",
                    description = responsibleProfessional,
                    timestamp = updatedAtEpochMillis,
                    timestampLabel = updatedAtEpochMillis.toRelativeLabel(now),
                    tone = PlusFisioStatusTone.Warning,
                    filter = ClientHistoryFilter.Profile
                )
            )
        }

        if (!notes.isNullOrBlank()) {
            add(
                HistoryEntry(
                    title = "Observacoes registradas",
                    description = notes,
                    timestamp = updatedAtEpochMillis,
                    timestampLabel = updatedAtEpochMillis.toRelativeLabel(now),
                    tone = PlusFisioStatusTone.Neutral,
                    filter = ClientHistoryFilter.Notes
                )
            )
        }
    }.sortedByDescending { entry -> entry.timestamp }
}

private fun ClientHistoryFilter.toLabel(): String {
    return when (this) {
        ClientHistoryFilter.All -> "Todos"
        ClientHistoryFilter.Registration -> "Cadastro"
        ClientHistoryFilter.Contact -> "Contato"
        ClientHistoryFilter.Profile -> "Perfil"
        ClientHistoryFilter.Notes -> "Notas"
    }
}

private fun Long.toRelativeLabel(now: Long): String {
    val days = ((now - this).coerceAtLeast(0L)) / (24L * 60L * 60L * 1000L)

    return when (days) {
        0L -> "Hoje"
        1L -> "Ontem"
        else -> "Ha ${days}d"
    }
}
