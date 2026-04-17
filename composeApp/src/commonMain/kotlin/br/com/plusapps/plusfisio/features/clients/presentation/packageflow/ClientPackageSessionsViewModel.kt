package br.com.plusapps.plusfisio.features.clients.presentation.packageflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.model.Client
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
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

class ClientPackageSessionsViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClientPackageSessionsState())
    val state = _state.asStateFlow()

    private val _events = Channel<ClientPackageSessionsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var boundStudioId: String? = null
    private var boundClientId: String? = null

    fun bind(session: AuthSession, clientId: String) {
        val studioId = session.studioId ?: return
        if (boundStudioId == studioId && boundClientId == clientId) return

        boundStudioId = studioId
        boundClientId = clientId
        loadClient(studioId, clientId)
    }

    fun onAction(action: ClientPackageSessionsAction) {
        when (action) {
            ClientPackageSessionsAction.OnBackClicked -> emitEvent(ClientPackageSessionsEvent.NavigateBack)
            ClientPackageSessionsAction.OnHomeTabClicked -> emitEvent(ClientPackageSessionsEvent.NavigateHome)
            ClientPackageSessionsAction.OnAgendaTabClicked,
            ClientPackageSessionsAction.OnFinanceTabClicked,
            ClientPackageSessionsAction.OnMoreTabClicked,
            ClientPackageSessionsAction.OnRegisterAttendanceClicked,
            ClientPackageSessionsAction.OnRegisterMissedClicked,
            ClientPackageSessionsAction.OnRenewPackageClicked,
            ClientPackageSessionsAction.OnAdjustBalanceClicked -> emitFeaturePendingMessage()
            ClientPackageSessionsAction.OnRetryClicked -> {
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
                    _state.value = client.toPackageState()
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
            ClientPackageSessionsEvent.ShowMessage(
                UiText.Resource(Res.string.clients_feature_pending)
            )
        )
    }

    private fun emitEvent(event: ClientPackageSessionsEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

private fun Client.toPackageState(): ClientPackageSessionsState {
    val packageTitle = if (primaryModality.isNullOrBlank()) {
        "Sem pacote ativo"
    } else {
        primaryModality
    }

    val packageDescription = if (primaryModality.isNullOrBlank()) {
        "Cliente pronto para vincular um pacote assim que o fluxo de sessoes entrar."
    } else {
        "$primaryModality pode virar o pacote principal deste acompanhamento."
    }

    val usageSummary = when {
        notes.isNullOrBlank() -> "Ainda nao ha consumo de sessoes registrado para este cliente."
        else -> "Observacoes recentes ajudam a organizar futuras presencas, faltas e renovacoes."
    }

    val nextRecommendation = if (responsibleProfessional.isNullOrBlank()) {
        "Definir o profissional responsavel ajuda a fechar a operacao antes de criar o pacote."
    } else {
        "Cliente ja tem contexto suficiente para receber pacote e saldo assim que a etapa operacional for conectada."
    }

    val historyItems = buildList {
        add("Cadastro do cliente pronto para receber pacote.")
        if (!primaryModality.isNullOrBlank()) add("Modalidade principal definida: $primaryModality.")
        if (!responsibleProfessional.isNullOrBlank()) add("Profissional responsavel: $responsibleProfessional.")
        if (!notes.isNullOrBlank()) add("Observacoes ajudam a orientar proximos passos do pacote.")
    }

    return ClientPackageSessionsState(
        isLoading = false,
        errorMessage = null,
        packageTitle = packageTitle,
        packageDescription = packageDescription,
        usageSummary = usageSummary,
        nextRecommendation = nextRecommendation,
        historyItems = historyItems
    )
}
