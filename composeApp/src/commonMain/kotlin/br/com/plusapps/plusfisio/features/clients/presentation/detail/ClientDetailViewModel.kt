package br.com.plusapps.plusfisio.features.clients.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.model.Client
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.components.PlusFisioStatusTone
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.domain.ClientRepository
import br.com.plusapps.plusfisio.features.clients.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_feature_pending

class ClientDetailViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClientDetailState())
    val state = _state.asStateFlow()

    private val _events = Channel<ClientDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var boundStudioId: String? = null
    private var boundClientId: String? = null
    private var boundRefreshVersion: Int? = null

    fun bind(
        session: AuthSession,
        clientId: String,
        refreshVersion: Int
    ) {
        val studioId = session.studioId ?: return
        if (
            boundStudioId == studioId &&
            boundClientId == clientId &&
            boundRefreshVersion == refreshVersion
        ) {
            return
        }

        boundStudioId = studioId
        boundClientId = clientId
        boundRefreshVersion = refreshVersion
        loadClient(studioId, clientId)
    }

    fun onAction(action: ClientDetailAction) {
        when (action) {
            ClientDetailAction.OnAgendaTabClicked,
            ClientDetailAction.OnChargeClicked,
            ClientDetailAction.OnFinanceTabClicked,
            ClientDetailAction.OnMoreTabClicked,
            ClientDetailAction.OnPresenceClicked,
            ClientDetailAction.OnWhatsappClicked -> emitFeaturePendingMessage()

            ClientDetailAction.OnBackClicked -> emitEvent(ClientDetailEvent.NavigateBack)
            ClientDetailAction.OnEditClicked -> boundClientId?.let { emitEvent(ClientDetailEvent.NavigateToEdit(it)) }
            ClientDetailAction.OnHistoryClicked -> boundClientId?.let { emitEvent(ClientDetailEvent.NavigateToHistory(it)) }
            ClientDetailAction.OnHomeTabClicked -> emitEvent(ClientDetailEvent.NavigateHome)
            ClientDetailAction.OnPackageClicked -> boundClientId?.let { emitEvent(ClientDetailEvent.NavigateToPackage(it)) }
            ClientDetailAction.OnRetryClicked -> {
                val studioId = boundStudioId ?: return
                val clientId = boundClientId ?: return
                loadClient(studioId, clientId)
            }
        }
    }

    private fun loadClient(studioId: String, clientId: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            clientRepository.getClient(studioId, clientId)
                .onSuccess { client ->
                    _state.value = client.toDetailState()
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

    private fun emitFeaturePendingMessage() {
        emitEvent(
            ClientDetailEvent.ShowMessage(
                UiText.Resource(Res.string.clients_feature_pending)
            )
        )
    }

    private fun emitEvent(event: ClientDetailEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

private fun Client.toDetailState(): ClientDetailState {
    val subtitlePieces = buildList {
        if (!primaryModality.isNullOrBlank()) add(primaryModality)
        if (!whatsappPhone.isNullOrBlank()) add("WhatsApp preferencial")
    }

    val summaryDescription = when {
        responsibleProfessional.isNullOrBlank() && acquisitionSource.isNullOrBlank() ->
            "Cadastro ainda pode ganhar contexto operacional para facilitar a rotina do estudio."

        responsibleProfessional.isNullOrBlank() ->
            "Cliente ativo. Defina o profissional responsavel para fechar a operacao deste cadastro."

        else ->
            "Cliente ativo, com contato pronto para agenda, cobranca simples e acompanhamentos futuros."
    }

    val packageDescription = if (primaryModality.isNullOrBlank()) {
        "Sem pacote ativo. O cliente pode seguir avulso ou receber um pacote na proxima etapa do fluxo."
    } else {
        "$primaryModality pronta para virar pacote quando o fluxo de sessoes entrar."
    }

    val historyDescription = when {
        notes.isNullOrBlank() ->
            "O historico ja registra cadastro, atualizacoes e campos preenchidos neste cliente."

        else ->
            "Observacoes e ajustes recentes ajudam a manter o acompanhamento operacional visivel."
    }

    return ClientDetailState(
        isLoading = false,
        errorMessage = null,
        clientId = clientId,
        name = fullName,
        subtitle = subtitlePieces.joinToString(" • ").ifBlank { "Cliente ativo" },
        statusLabel = "Ativo",
        statusTone = PlusFisioStatusTone.Success,
        nextStepDescription = "Sem agendamento futuro. Cliente ativo e pronto para novos retornos.",
        summaryDescription = summaryDescription,
        packageDescription = packageDescription,
        historyDescription = historyDescription
    )
}
