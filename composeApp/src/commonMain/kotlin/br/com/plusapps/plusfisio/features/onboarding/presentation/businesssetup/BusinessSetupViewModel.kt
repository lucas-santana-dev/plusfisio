package br.com.plusapps.plusfisio.features.onboarding.presentation.businesssetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.plusapps.plusfisio.core.domain.model.BusinessType
import br.com.plusapps.plusfisio.core.domain.model.TeamSizeRange
import br.com.plusapps.plusfisio.core.domain.onFailure
import br.com.plusapps.plusfisio.core.domain.onSuccess
import br.com.plusapps.plusfisio.core.presentation.input.cityStateSuggestions
import br.com.plusapps.plusfisio.core.presentation.input.isValidCityState
import br.com.plusapps.plusfisio.core.presentation.input.normalizeCityState
import br.com.plusapps.plusfisio.core.presentation.text.UiText
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioInput
import br.com.plusapps.plusfisio.features.onboarding.domain.BootstrapStudioUseCase
import br.com.plusapps.plusfisio.features.onboarding.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plusfisio.composeapp.generated.resources.Res
import plusfisio.composeapp.generated.resources.onboarding_error_business_type_required
import plusfisio.composeapp.generated.resources.onboarding_error_city_state_invalid
import plusfisio.composeapp.generated.resources.onboarding_error_city_state_required
import plusfisio.composeapp.generated.resources.onboarding_error_studio_name_required
import plusfisio.composeapp.generated.resources.onboarding_error_team_size_required

class BusinessSetupViewModel(
    private val bootstrapStudioUseCase: BootstrapStudioUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BusinessSetupState())
    val state = _state.asStateFlow()

    private val _events = Channel<BusinessSetupEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: BusinessSetupAction) {
        when (action) {
            is BusinessSetupAction.OnBusinessNameChanged -> _state.update {
                it.copy(businessName = action.value, businessNameError = null)
            }

            is BusinessSetupAction.OnBusinessTypeSelected -> _state.update {
                it.copy(businessType = action.value, businessTypeError = null)
            }

            is BusinessSetupAction.OnCityStateChanged -> _state.update {
                it.copy(
                    cityState = action.value,
                    cityStateSuggestions = cityStateSuggestions(action.value),
                    cityStateError = null
                )
            }

            is BusinessSetupAction.OnTeamSizeSelected -> _state.update {
                it.copy(teamSize = action.value, teamSizeError = null)
            }

            is BusinessSetupAction.OnCityStateSuggestionSelected -> _state.update {
                it.copy(
                    cityState = action.value,
                    cityStateSuggestions = emptyList(),
                    cityStateError = null
                )
            }

            BusinessSetupAction.OnSubmitClicked -> submit()
        }
    }

    fun resetState() {
        _state.value = BusinessSetupState()
    }

    private fun submit() {
        val current = state.value
        val nameError = validateBusinessName(current.businessName)
        val businessTypeError = validateBusinessType(current.businessType)
        val teamSizeError = validateTeamSize(current.teamSize)
        val cityStateError = validateCityState(current.cityState)

        if (nameError != null || businessTypeError != null || teamSizeError != null || cityStateError != null) {
            _state.update {
                it.copy(
                    businessNameError = nameError,
                    businessTypeError = businessTypeError,
                    teamSizeError = teamSizeError,
                    cityStateError = cityStateError,
                    cityStateSuggestions = if (cityStateError == null) emptyList() else it.cityStateSuggestions
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }
            bootstrapStudioUseCase(
                BootstrapStudioInput(
                    businessName = current.businessName.trim(),
                    businessType = requireNotNull(current.businessType),
                    teamSize = requireNotNull(current.teamSize),
                    cityState = normalizeCityState(current.cityState)
                )
            ).onSuccess {
                _state.update { it.copy(isSubmitting = false, cityStateSuggestions = emptyList()) }
                _events.send(BusinessSetupEvent.Completed)
            }.onFailure { error ->
                _state.update { it.copy(isSubmitting = false, cityStateSuggestions = emptyList()) }
                _events.send(BusinessSetupEvent.ShowMessage(error.toUiText()))
            }
        }
    }

    private fun validateBusinessName(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.onboarding_error_studio_name_required)
        return null
    }

    private fun validateBusinessType(value: BusinessType?): UiText? {
        if (value == null) return UiText.Resource(Res.string.onboarding_error_business_type_required)
        return null
    }

    private fun validateTeamSize(value: TeamSizeRange?): UiText? {
        if (value == null) return UiText.Resource(Res.string.onboarding_error_team_size_required)
        return null
    }

    private fun validateCityState(value: String): UiText? {
        if (value.isBlank()) return UiText.Resource(Res.string.onboarding_error_city_state_required)
        if (!isValidCityState(value)) return UiText.Resource(Res.string.onboarding_error_city_state_invalid)
        return null
    }
}
