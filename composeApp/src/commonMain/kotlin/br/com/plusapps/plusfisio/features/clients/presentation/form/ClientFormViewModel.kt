package br.com.plusapps.plusfisio.features.clients.presentation.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.input.extractDigits
import br.com.plusapps.plusfisio.core.presentation.input.formatBrazilPhone
import br.com.plusapps.plusfisio.core.presentation.input.isValidBrazilPhone
import br.com.plusapps.plusfisio.core.presentation.input.isValidEmail
import br.com.plusapps.plusfisio.core.presentation.input.sanitizeEmail
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.auth.domain.AuthSession
import br.com.plusapps.plusfisio.features.clients.domain.ClientRepository
import br.com.plusapps.plusfisio.features.clients.domain.CreateClientInput
import br.com.plusapps.plusfisio.features.clients.domain.UpdateClientInput
import br.com.plusapps.plusfisio.features.clients.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.clients_birth_date_invalid
import plusfisio.composeapp.generated.resources.clients_email_invalid
import plusfisio.composeapp.generated.resources.clients_feature_pending
import plusfisio.composeapp.generated.resources.clients_name_required
import plusfisio.composeapp.generated.resources.clients_phone_invalid
import plusfisio.composeapp.generated.resources.clients_phone_required

class ClientFormViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClientFormState())
    val state = _state.asStateFlow()

    private val _events = Channel<ClientFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var boundSession: AuthSession? = null
    private var boundClientId: String? = null
    private var boundMode: ClientFormMode? = null

    fun bind(
        session: AuthSession,
        mode: ClientFormMode,
        clientId: String?
    ) {
        if (
            boundSession?.studioId == session.studioId &&
            boundClientId == clientId &&
            boundMode == mode
        ) {
            return
        }

        boundSession = session
        boundClientId = clientId
        boundMode = mode

        if (mode == ClientFormMode.Create) {
            _state.value = ClientFormState(mode = ClientFormMode.Create)
            return
        }

        val studioId = session.studioId ?: return
        if (clientId.isNullOrBlank()) {
            _events.trySend(ClientFormEvent.NavigateBack)
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    mode = ClientFormMode.Edit,
                    isLoading = true,
                    errorMessage = null
                )
            }

            clientRepository.getClient(studioId, clientId)
                .onSuccess { client ->
                    _state.update {
                        it.copy(
                            mode = ClientFormMode.Edit,
                            isLoading = false,
                            fullName = client.fullName,
                            phone = client.whatsappPhone ?: client.phone,
                            email = client.email.orEmpty(),
                            birthDate = client.birthDate.orEmpty(),
                            primaryModality = client.primaryModality.orEmpty(),
                            responsibleProfessional = client.responsibleProfessional.orEmpty(),
                            acquisitionSource = client.acquisitionSource.orEmpty(),
                            notes = client.notes.orEmpty(),
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            mode = ClientFormMode.Edit,
                            isLoading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
        }
    }

    fun onAction(action: ClientFormAction) {
        when (action) {
            is ClientFormAction.OnAcquisitionSourceChanged -> _state.update {
                it.copy(acquisitionSource = action.value, errorMessage = null)
            }

            ClientFormAction.OnAgendaTabClicked,
            ClientFormAction.OnFinanceTabClicked,
            ClientFormAction.OnMoreTabClicked -> emitFeaturePendingMessage()

            ClientFormAction.OnBackClicked -> emitEvent(ClientFormEvent.NavigateBack)
            is ClientFormAction.OnBirthDateChanged -> _state.update {
                it.copy(
                    birthDate = formatBirthDate(action.value),
                    birthDateError = null,
                    errorMessage = null
                )
            }

            is ClientFormAction.OnEmailChanged -> _state.update {
                it.copy(
                    email = action.value,
                    emailError = null,
                    errorMessage = null
                )
            }

            is ClientFormAction.OnFullNameChanged -> _state.update {
                it.copy(
                    fullName = action.value,
                    fullNameError = null,
                    errorMessage = null
                )
            }

            ClientFormAction.OnHomeTabClicked -> emitEvent(ClientFormEvent.NavigateHome)
            is ClientFormAction.OnNotesChanged -> _state.update {
                it.copy(notes = action.value, errorMessage = null)
            }

            is ClientFormAction.OnPhoneChanged -> _state.update {
                it.copy(
                    phone = extractDigits(action.value).take(11),
                    phoneError = null,
                    errorMessage = null
                )
            }

            is ClientFormAction.OnPrimaryModalityChanged -> _state.update {
                it.copy(primaryModality = action.value, errorMessage = null)
            }

            is ClientFormAction.OnResponsibleProfessionalChanged -> _state.update {
                it.copy(responsibleProfessional = action.value, errorMessage = null)
            }

            ClientFormAction.OnRetryClicked -> {
                val session = boundSession ?: return
                bind(session, requireNotNull(boundMode), boundClientId)
            }

            ClientFormAction.OnSaveClicked -> saveClient()
        }
    }

    fun resetState() {
        boundSession = null
        boundClientId = null
        boundMode = null
        _state.value = ClientFormState()
    }

    private fun saveClient() {
        val currentState = _state.value
        val session = boundSession ?: return
        val studioId = session.studioId ?: return
        val fullNameError = validateName(currentState.fullName)
        val phoneError = validatePhone(currentState.phone)
        val emailError = validateEmail(currentState.email)
        val birthDateError = validateBirthDate(currentState.birthDate)

        if (
            fullNameError != null ||
            phoneError != null ||
            emailError != null ||
            birthDateError != null
        ) {
            _state.update {
                it.copy(
                    fullNameError = fullNameError,
                    phoneError = phoneError,
                    emailError = emailError,
                    birthDateError = birthDateError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }

            when (currentState.mode) {
                ClientFormMode.Create -> {
                    clientRepository.createClient(
                        CreateClientInput(
                            studioId = studioId,
                            fullName = currentState.fullName,
                            phone = extractDigits(currentState.phone),
                            whatsappPhone = extractDigits(currentState.phone),
                            email = sanitizeEmail(currentState.email).ifBlank { null },
                            birthDate = currentState.birthDate.ifBlank { null },
                            primaryModality = currentState.primaryModality.ifBlank { null },
                            responsibleProfessional = currentState.responsibleProfessional.ifBlank { null },
                            acquisitionSource = currentState.acquisitionSource.ifBlank { null },
                            notes = currentState.notes.ifBlank { null }
                        )
                    ).onSuccess { client ->
                        _state.update { it.copy(isSaving = false) }
                        emitEvent(ClientFormEvent.ClientSaved(client.clientId))
                    }.onFailure { error ->
                        _state.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = error.toUiText()
                            )
                        }
                    }
                }

                ClientFormMode.Edit -> {
                    val clientId = boundClientId ?: return@launch
                    clientRepository.updateClient(
                        UpdateClientInput(
                            studioId = studioId,
                            clientId = clientId,
                            fullName = currentState.fullName,
                            phone = extractDigits(currentState.phone),
                            whatsappPhone = extractDigits(currentState.phone),
                            email = sanitizeEmail(currentState.email).ifBlank { null },
                            birthDate = currentState.birthDate.ifBlank { null },
                            primaryModality = currentState.primaryModality.ifBlank { null },
                            responsibleProfessional = currentState.responsibleProfessional.ifBlank { null },
                            acquisitionSource = currentState.acquisitionSource.ifBlank { null },
                            notes = currentState.notes.ifBlank { null }
                        )
                    ).onSuccess { client ->
                        _state.update { it.copy(isSaving = false) }
                        emitEvent(ClientFormEvent.ClientSaved(client.clientId))
                    }.onFailure { error ->
                        _state.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = error.toUiText()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validateName(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.clients_name_required)
        return null
    }

    private fun validatePhone(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.clients_phone_required)
        if (!isValidBrazilPhone(value)) return UiText.Resource(Res.string.clients_phone_invalid)
        return null
    }

    private fun validateEmail(value: String): UiText? {
        if (value.isBlank()) return null
        if (!isValidEmail(value)) return UiText.Resource(Res.string.clients_email_invalid)
        return null
    }

    private fun validateBirthDate(value: String): UiText? {
        if (value.isBlank()) return null
        val digits = extractDigits(value)
        if (digits.length != 8) return UiText.Resource(Res.string.clients_birth_date_invalid)
        return null
    }

    private fun emitFeaturePendingMessage() {
        emitEvent(
            ClientFormEvent.ShowMessage(
                UiText.Resource(Res.string.clients_feature_pending)
            )
        )
    }

    private fun emitEvent(event: ClientFormEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

private fun formatBirthDate(value: String): String {
    val digits = extractDigits(value).take(8)

    return when {
        digits.isEmpty() -> ""
        digits.length <= 2 -> digits
        digits.length <= 4 -> "${digits.take(2)}/${digits.drop(2)}"
        else -> "${digits.take(2)}/${digits.drop(2).take(2)}/${digits.drop(4)}"
    }
}
