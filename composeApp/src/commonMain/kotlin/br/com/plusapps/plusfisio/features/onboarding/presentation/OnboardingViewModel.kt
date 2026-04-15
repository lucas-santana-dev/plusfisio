package br.com.plusapps.plusfisio.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioInput
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.onboarding_error_phone_required
import plusfisio.composeapp.generated.resources.onboarding_error_studio_name_required

class OnboardingViewModel(
    private val bootstrapStudioUseCase: BootstrapStudioUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>(capacity = Channel.BUFFERED)
    val events: Flow<OnboardingEvent> = _events.receiveAsFlow()

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnBusinessTypeSelected -> _state.update { it.copy(selectedBusinessType = action.value) }
            is OnboardingAction.OnPhoneChanged -> _state.update { it.copy(phone = action.value, phoneError = null) }
            is OnboardingAction.OnStudioNameChanged -> _state.update { it.copy(studioName = action.value, studioNameError = null) }
            is OnboardingAction.OnTimezoneChanged -> _state.update { it.copy(timezone = action.value) }
            is OnboardingAction.OnWhatsappChanged -> _state.update { it.copy(whatsappPhone = action.value) }
            OnboardingAction.OnSubmitClicked -> submit()
        }
    }

    private fun submit() {
        val current = state.value
        val studioNameError = validateStudioName(current.studioName)
        val phoneError = validatePhone(current.phone)

        if (studioNameError != null || phoneError != null) {
            _state.update { it.copy(studioNameError = studioNameError, phoneError = phoneError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }
            bootstrapStudioUseCase(
                BootstrapStudioInput(
                    studioName = current.studioName.trim(),
                    businessType = current.selectedBusinessType,
                    phone = current.phone.trim(),
                    whatsappPhone = current.whatsappPhone.trim(),
                    timezone = current.timezone.trim()
                )
            ).onSuccess {
                _state.update { state -> state.copy(isSubmitting = false) }
                emitEvent(OnboardingEvent.Completed)
            }.onFailure { error ->
                _state.update { state -> state.copy(isSubmitting = false) }
                emitEvent(OnboardingEvent.ShowMessage(error.toUiText()))
            }
        }
    }

    private fun validateStudioName(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.onboarding_error_studio_name_required)
        return null
    }

    private fun validatePhone(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.onboarding_error_phone_required)
        return null
    }

    private fun emitEvent(event: OnboardingEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
